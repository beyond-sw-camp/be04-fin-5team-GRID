package org.highfives.grid.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.user.command.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final String accessTokenExpTime;
    private final String refreshTokenExpTime;
    private final UserService userService;
    private final Environment environment;

    public JwtUtil(
            @Value("${application.security.jwt.secretKey}") String secretKey,
            @Value("${application.security.jwt.expiration_time}") String accessTokenExpTime,
            @Value("${application.security.jwt.refresh-token.expiration_time}") String refreshTokenExpTime,
            UserService userService,
            Environment environment
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
        this.userService = userService;
        this.environment = environment;
    }

    /* 설명. 넘어온 AccessToken으로 인증 객체 추출 */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getUserEmail(token));

        /* 설명. 토큰에서 claim들 추출(토큰 복호화) */
        Claims claims = parseClaims(token);
        System.out.println("넘어온 AccessToekn claims 확인: " + claims);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        /* 설명. 클레임에서 권한 정보 가져오기 */
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().replace("[", "").replace("]", "").split(", "))	// ex: "ROLE_ADMIN"이랑 "ROLE_MEMBER"같은 문자열이 들어있는 문자열 배열
                        .map(role -> new SimpleGrantedAuthority(role))   				// 문자열 배열에 들어있는 권한 문자열 마다 SimpleGrantedAuthority 객체로 만듦
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    /* 설명. Token 검증 */
    public boolean validateToken(String token) {
        try {
            if (isTokenExpired(token)) {
                log.info("Expired JWT Token");
                throw new ExpiredJwtException(null, null, "Expired JWT Token");
            }
            if (!isTokenStructureValid(token)) {
                log.info("Invalid JWT Token structure");
                throw new MalformedJwtException("Invalid JWT Token structure");
            }
            if (isTokenUnsupported(token)) {
                log.info("Unsupported JWT Token");
                throw new UnsupportedJwtException("Unsupported JWT Token");
            }
            if (isTokenClaimsEmpty(token)) {
                log.info("JWT claims string is empty");
                throw new IllegalArgumentException("JWT claims string is empty");
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Expired JWT Token");
        } catch (MalformedJwtException | SecurityException e) {
            throw new MalformedJwtException("Invalid JWT Token structure");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private boolean isTokenStructureValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | SecurityException e) {
            return false;
        }
    }

    private boolean isTokenUnsupported(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return false;
        } catch (UnsupportedJwtException e) {
            return true;
        }
    }

    private boolean isTokenClaimsEmpty(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims == null || claims.isEmpty();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /* 설명. Token에서 User 정보 추출 */
    public int getUserId(String token) {
        return parseClaims(token).get("id", Integer.class);
    }

    public String getUserEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰에서 유저 role 추출
    public String getUserRole(String token) {
        return parseClaims(token).get("auth").toString();
    }

    /* 설명. access / refresh 토큰 판별 */
    public String getCategory(String token) {
        return parseClaims(token).get("category", String.class);
    }

    /* 설명. Token에서 Claims 추출 */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //토큰 생성
    public String createJwt(Claims claims, String category) {

        String expiration = null;

        if( category.equals("refresh")){
            expiration = environment.getProperty("application.security.jwt.refresh-token.expiration_time");
            claims.put("category", "refresh");

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                    .signWith(SignatureAlgorithm.HS512,
                            environment.getProperty("application.security.jwt.secretKey"))
                    .compact();
        }
        else if (category.equals("access")){
            expiration = environment.getProperty("application.security.jwt.expiration_time");
            claims.put("category", "access");

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                    .signWith(SignatureAlgorithm.HS512,
                            environment.getProperty("application.security.jwt.secretKey"))
                    .compact();
        }

        throw new RuntimeException("토큰을 생성할 수 없습니다.");
    }

    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }
}

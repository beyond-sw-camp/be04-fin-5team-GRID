package org.highfives.grid.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.user.command.aggregate.Role;
import org.highfives.grid.user.command.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    private final UserService userService;

    public JwtUtil(
            @Value("${application.security.jwt.secretKey}") String secretKey,
            @Value("${application.security.jwt.expiration_time}") long accessTokenExpTime,
            @Value("${application.security.jwt.refresh-token.expiration_time}") long refreshTokenExpTime,
            UserService userService
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
        this.userService = userService;
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
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token {}", e);
        } catch (ExpiredJwtException e) {
            log.info("expired JWT Toekn {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims strig si empty {}", e);
        }

        return true;
    }

    /* 설명. Token에서 User의 Id 개념 추출 */
    public String getUserEmail(String token) {
        return parseClaims(token).get("email").toString();
    }

    // 토큰에서 유저 role 추출
    public String getUserRole(String token) {
        return parseClaims(token).get("role").toString();
    }


    /* 설명. Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /* 설명. access / refresh 토큰 판별 */
    public String getCategory(String token) {

        return parseClaims(token).get("category", String.class);
    }
}

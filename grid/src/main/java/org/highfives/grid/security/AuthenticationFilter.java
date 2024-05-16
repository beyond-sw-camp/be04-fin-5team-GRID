package org.highfives.grid.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.highfives.grid.user.command.aggregate.PrincipalDetails;
import org.highfives.grid.user.command.aggregate.Role;
import org.highfives.grid.user.command.vo.ReqLogin;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment environment;


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            ReqLogin requestLogin =
                    new ObjectMapper().readValue(request.getInputStream(), ReqLogin.class);

            // 사용자가 전달한 id / pwd 를 사용해 authentication 토큰을 만듬
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestLogin.getEmail(), requestLogin.getPwd(), new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        // Authentication 객체로부터 유저 정보를 읽어와 Claims 에 담는 부분
        int userId = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getId();
        Role hasRole = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getRole();
        String email = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getEmail();

        Claims claims = Jwts.claims().setSubject(((PrincipalDetails)auth.getPrincipal()).getEmployee().getEmail());
        claims.put("id", userId);
        claims.put("auth", hasRole);
        claims.put("email", email);

        // 토큰 생성(Refresh, Access)
        String refreshToken = createJwt("refresh", claims);
        String accessToken = createJwt("access", claims);

        // 생성한 토큰 헤더에 저장
        response.addHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
    }

    private String createJwt(String category, Claims claims) {

        String expiration = null;
        if( category.equals("refresh"))
            expiration = environment.getProperty("application.security.jwt.refresh-token.expiration_time");
        if( category.equals("access"))
            expiration = environment.getProperty("application.security.jwt.expiration_time");

        return  Jwts.builder()
                .claim("category", category)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("application.security.jwt.secretKey"))
                .compact();
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }
}

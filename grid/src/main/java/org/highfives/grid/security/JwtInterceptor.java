package org.highfives.grid.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final Environment env;

    @Autowired
    public JwtInterceptor(Environment env) {
        this.env = env;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("Interceptor invoked");
        System.out.println(" 리스폰스 헤더 " + request.getHeader("Authorization"));
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            System.out.println("Interceptor invoked22");
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(env.getProperty("application.security.jwt.secretKey"))
                    .build()
                    .parseClaimsJws(token);

            System.out.println("여기까지 통과하는지? : " + token);
            request.setAttribute("claims", claims.getBody());
        }

        return true;
    }
}

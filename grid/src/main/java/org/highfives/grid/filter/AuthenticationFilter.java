package org.highfives.grid.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.aggregate.PrincipalDetails;
import org.highfives.grid.user.command.aggregate.Role;
import org.highfives.grid.user.command.service.UserService;
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

    private final UserService userService;
    private final Environment environment;


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService, Environment environment) {
        super(authenticationManager);
        this.userService = userService;
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

        int userId = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getId();
        Role hasRole = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getRole();

//        Claims claims = Jwts.claims().setSubject(userDetails.getEmail());
        Claims claims = Jwts.claims();
        claims.put("id", userId);
        claims.put("auth", hasRole);

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(environment.getProperty("application.security.jwt.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("application.security.jwt.secretKey"))
                .compact();

        System.out.println("token = " + token);
        response.addHeader("token", token);
    }
}

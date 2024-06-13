package org.highfives.grid.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.highfives.grid.user.command.aggregate.PrincipalDetails;
import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.highfives.grid.user.command.aggregate.Role;
import org.highfives.grid.user.command.repository.TokenReissueRepository;
import org.highfives.grid.user.command.vo.ReqLoginVO;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment environment;
    private final TokenReissueRepository tokenReissueRepository;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Environment environment,
                                TokenReissueRepository tokenReissueRepository) {
        super(authenticationManager);
        this.environment = environment;
        this.tokenReissueRepository = tokenReissueRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            ReqLoginVO requestLogin =
                    new ObjectMapper().readValue(request.getInputStream(), ReqLoginVO.class);

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

        JwtUtil jwtUtil = new JwtUtil(environment.getProperty("application.security.jwt.secretKey"),
                environment.getProperty("application.security.jwt.expiration_time"),
                environment.getProperty("application.security.jwt.refresh-token.expiration_time"),
                null, environment);

        // Authentication 객체로부터 유저 정보를 읽어와 Claims 에 담는 부분
        int userId = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getId();
        Role role = ((PrincipalDetails)auth.getPrincipal()).getEmployee().getRole();

        Claims claims = Jwts.claims().setSubject(((PrincipalDetails)auth.getPrincipal()).getEmployee().getEmail());
        claims.put("id", userId);
        claims.put("auth", role);

        // 토큰 생성(Refresh, Access)
        String accessToken = jwtUtil.createJwt(claims, "access");
        String refreshToken = jwtUtil.createJwt(claims, "refresh");

        RefreshToken redisToken = new RefreshToken(userId, refreshToken);
        tokenReissueRepository.save(redisToken);

        // 생성한 토큰 data/쿠키에 저장
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
//        response.addHeader("Set-Cookie", jwtUtil.createCookie("refresh", refreshToken).toString());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"access\": \"" + accessToken + "\"}");
        out.flush();
    }

}

package org.highfives.grid.user.command.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.security.JwtUtil;
import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.highfives.grid.user.command.repository.TokenReissueRepository;
import org.highfives.grid.user.exception.InvalidInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

@Service("TokenReissueService")
@Slf4j
public class TokenReissueServiceImpl implements TokenReissueService{

    private final JwtUtil jwtUtil;
    private final TokenReissueRepository tokenReissueRepository;

    @Autowired
    public TokenReissueServiceImpl(JwtUtil jwtUtil,
                                   TokenReissueRepository tokenReissueRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenReissueRepository = tokenReissueRepository;
    }

    @Override
    public ResponseEntity<?> checkValidation(String refresh) {

        if (refresh == null || refresh.isEmpty())
            throw new InvalidInfoException("Refresh Token is empty");

        //토큰 유효성 체크 & refresh 토큰 체크
        try {
            jwtUtil.validateToken(refresh);
            return null;
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Refresh token expired", HttpStatus.BAD_REQUEST);
        } catch (MalformedJwtException | SecurityException e) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException e) {
            return new ResponseEntity<>("Unsupported refresh Token", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("JWT claims string is empty", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> checkRefreshToken(String refresh) {

        if(refresh == null || refresh.isEmpty())
            throw new InvalidInfoException("Token is empty");

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteOldToken(String accessToken) {

        tokenReissueRepository.findById(jwtUtil.getUserId(accessToken))
                .ifPresent(tokenReissueRepository::delete);
    }

    @Override
    @Transactional
    public HttpServletResponse reissueToken(String refresh, HttpServletResponse response) throws IOException {

        int id = jwtUtil.getUserId(refresh);
        String username = jwtUtil.getUserEmail(refresh);
        String role = jwtUtil.getUserRole(refresh);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", id);
        claims.put("auth", role);
        claims.put("category", "access");

        //신규 토큰 생성(access/refresh)
        String newAccess = jwtUtil.createJwt(claims, "access");
        String newRefresh = jwtUtil.createJwt(claims, "refresh");

        // 생성한 refresh 토큰 redis에 저장
        RefreshToken redisToken = new RefreshToken(id, newRefresh);
        tokenReissueRepository.save(redisToken);

        //response
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));
//        response.addHeader("Set-Cookie", jwtUtil.createCookie("refresh", newRefresh).toString());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"access\": \"" + newAccess + "\"}");
        out.flush();
        return response;
    }
}

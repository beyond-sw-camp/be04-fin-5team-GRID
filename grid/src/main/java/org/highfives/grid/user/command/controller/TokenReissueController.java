package org.highfives.grid.user.command.controller;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.highfives.grid.security.JwtUtil;
import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.highfives.grid.user.command.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenReissueController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public TokenReissueController(JwtUtil jwtUtil,
                                  RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        //expired check
        try {
            jwtUtil.validateToken(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Refresh token expired", HttpStatus.BAD_REQUEST);
        } catch (MalformedJwtException | SecurityException e) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException e) {
            return new ResponseEntity<>("Unsupported refresh Token", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("JWT claims string is empty", HttpStatus.BAD_REQUEST);

        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        int id = jwtUtil.getUserId(refresh);
        String username = jwtUtil.getUserEmail(refresh);
        String role = jwtUtil.getUserRole(refresh);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", id);
        claims.put("auth", role);
        claims.put("category", "access");

        //make new JWT
        String newAccess = jwtUtil.createJwt(claims, "access");
        String newRefresh = jwtUtil.createJwt(claims, "refresh");

        // 생성한 refresh 토큰 redis에 저장
        RefreshToken redisToken = new RefreshToken(newRefresh, id);
        refreshTokenRepository.save(redisToken);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}


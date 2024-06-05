package org.highfives.grid.user.command.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.highfives.grid.user.command.service.TokenReissueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TokenReissueController {

    private final TokenReissueService tokenReissueService;

    @Autowired
    public TokenReissueController(TokenReissueService tokenReissueService) {
        this.tokenReissueService = tokenReissueService;
    }

    @PostMapping("/tokens/re-auth")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String cookieHeader = request.getHeader("Cookie");
        System.out.println("cookieHeader = " + cookieHeader);
        String refresh = null;

        // Parse the Cookie header to find the refresh token
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                if (cookie.startsWith("refresh=")) {
                    refresh = cookie.substring("refresh=".length());
                    break;
                }
            }
        }

        // Check if refresh token is null
        if (refresh == null) {
            return new ResponseEntity<>("Refresh token not found", HttpStatus.BAD_REQUEST);
        }

        // Token validation and reissue logic
        try {
            tokenReissueService.checkValidation(refresh);
            tokenReissueService.checkRefreshToken(refresh);
            tokenReissueService.deleteOldToken(refresh);
            response = tokenReissueService.reissueToken(refresh, response);
        } catch (Exception e) {
            return new ResponseEntity<>("Token reissue failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}


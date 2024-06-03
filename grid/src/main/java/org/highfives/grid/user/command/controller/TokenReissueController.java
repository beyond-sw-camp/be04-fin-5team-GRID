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

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }
        tokenReissueService.checkValidation(refresh);
        tokenReissueService.checkRefreshToken(refresh);
        tokenReissueService.deleteOldToken(refresh);
        response = tokenReissueService.reissueToken(refresh, response);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}


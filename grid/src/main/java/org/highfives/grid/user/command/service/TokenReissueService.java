package org.highfives.grid.user.command.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface TokenReissueService {
    ResponseEntity<?> checkValidation(String refresh);

    ResponseEntity<?> checkRefreshToken(String refresh);

    HttpServletResponse reissueToken(String refresh, HttpServletResponse response) throws IOException;

    void deleteOldToken(String accessToken);
}

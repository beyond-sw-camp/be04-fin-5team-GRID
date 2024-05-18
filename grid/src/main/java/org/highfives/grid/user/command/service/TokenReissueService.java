package org.highfives.grid.user.command.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface TokenReissueService {
    ResponseEntity<?> checkValidation(String refresh);

    ResponseEntity<?> checkRefreshToken(String refresh);

    HttpServletResponse reissueToken(String refresh, HttpServletResponse response);

    void deleteOldToken(String accessToken);
}

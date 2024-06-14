//package org.highfives.grid.user.command.service;
//
//import org.highfives.grid.user.exception.InvalidInfoException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
///* Redis 를 사용하거나, 토큰 값에 따라 성공/실패가 나뉘는 케이스에 대해서는 별도로 정의하지 않음 */
//@SpringBootTest
//class TokenReissueServiceImplTest {
//
//    private TokenReissueService tokenReissueService;
//
//    @Autowired
//    public TokenReissueServiceImplTest(TokenReissueService tokenReissueService) {
//        this.tokenReissueService = tokenReissueService;
//    }
//
//    @DisplayName("토큰 유효성 체크 테스트 - 비어있는 토큰 전달 시")
//    @Test
//    void checkValidationInvalidTest() {
//        String refreshToken = "";
//
//        Assertions.assertThrows(InvalidInfoException.class,
//                () -> tokenReissueService.checkValidation(refreshToken)
//        );
//    }
//
//    @DisplayName("토큰 유형 체크 테스트 - 정상 동작시")
//    @Test
//    void checkRefreshTokenNormalTest() {
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJncmlkcGVvcGxlMjAy" +
//                "NEBnbWFpbC5jb20iLCJpZCI6MSwiYXV0aCI6IlJPTEVfQURNSU4iLCJjYXR" +
//                "lZ29yeSI6InJlZnJlc2giLCJpYXQiOjE3MTgyMzg4MjAsImV4cCI6MTcxODMy" +
//                "NTIyMH0.nU4_4ulTlbYlMSSziT2ZQHki-vOsyMHVtLkfhoYGYO3BB0oJmz4cz" +
//                "9vu9XHlqK_LTbXYSriPBrSCdsU1lTQfww";
//
//        Assertions.assertNull(tokenReissueService.checkRefreshToken(token));
//    }
//
//    @DisplayName("토큰 유형 체크 테스트 - 잘못된 토큰 전달 시")
//    @Test
//    void checkRefreshTokenInvalidTest() {
//
//        // access 토큰 전달 시
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJncmlkcGVvcGxlMjAyN" +
//                "EBnbWFpbC5jb20iLCJpZCI6MSwiYXV0aCI6IlJPTEVfQURNSU4iLCJjYXRl" +
//                "Z29yeSI6ImFjY2VzcyIsImlhdCI6MTcxODIzODgyMCwiZXhwIjoxNzE4MzI" +
//                "1MjIwfQ.9EtvzfJraTy7lEo23627SzdEkD8HWm7gzKkOtPS7WTJts6CDxi-k" +
//                "EcA4_oZqhP8qkvRdpHHVtcJgW2VB72WgMQ";
//
//        // 빈 토큰 전달 시
//        String token2 = "";
//
//        Assertions.assertNotNull(tokenReissueService.checkRefreshToken(token));
//        Assertions.assertThrows(InvalidInfoException.class,
//                () -> tokenReissueService.checkRefreshToken(token2));
//    }
//}
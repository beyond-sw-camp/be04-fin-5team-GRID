//package org.highfives.grid.work.query.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class BtApprovalServiceImplTest {
//
//    @Autowired
//    private BtApprovalService btApprovalService;
//
//
//    @DisplayName("승인된 출장 전체 조회")
//    @Test
//    void findAllBt() {
//        Assertions.assertDoesNotThrow(
//                () -> btApprovalService.findAllBt()
//        );
//    }
//
//    @DisplayName("승인된 출장 직원별 조회")
//    @Test
//    void findBtByEmployeeId() {
//        Assertions.assertDoesNotThrow(
//                () -> btApprovalService.findBtByEmployeeId(1)
//        );
//    }
//}
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
//class RwApprovalServiceImplTest {
//
//    @Autowired
//    private RwApprovalServiceImpl rwApprovalService;
//
//    @DisplayName("승인된 단축근무 전체 조회")
//    @Test
//    void findAllRw() {
//        Assertions.assertDoesNotThrow(
//                () -> rwApprovalService.findAllRw()
//        );
//    }
//
//    @DisplayName("승인된 단축근무 직원별 조회")
//    @Test
//    void findRwByEmployeeId() {
//        int employeeId = 2;
//        Assertions.assertDoesNotThrow(
//                () -> rwApprovalService.findRwByEmployeeId(employeeId)
//        );
//    }
//}
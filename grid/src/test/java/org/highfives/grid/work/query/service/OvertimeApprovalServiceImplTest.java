//package org.highfives.grid.work.query.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class OvertimeApprovalServiceImplTest {
//
//    @Autowired
//    private OvertimeApprovalServiceImpl overtimeApprovalService;
//
//    @DisplayName("승인된 시간 외 근무 전체 조회")
//    @Test
//    public void findAllOvertime() {
//        Assertions.assertDoesNotThrow(
//                () -> overtimeApprovalService.findOvertimeAll()
//        );
//    }
//
//    @DisplayName("승인된 시간 외 근무 1번 직원 조회")
//    @Test
//    public void findOvertimeByEmployeeId() {
//        Assertions.assertDoesNotThrow(
//                () -> overtimeApprovalService.findOvertimeByEmployeeId(1)
//        );
//    }
//}

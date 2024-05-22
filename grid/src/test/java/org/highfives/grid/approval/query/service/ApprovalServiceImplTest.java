package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApprovalServiceImplTest {

    @Autowired
    private ApprovalServiceImpl approvalService;

    @Test
    void findBTApprovalById() {
    }

    @DisplayName("직원별 주당 시간 외 근무 조회")
    @Test
    void findOInWeekByEmployeeId() {

        OvertimeInWeekDTO overtime = new OvertimeInWeekDTO("2024-05-12 00:00:00", "2024-05-18 00:00:00", 1);

        Assertions.assertDoesNotThrow(
                () -> approvalService.findOInWeekByEmployeeId(overtime)
        );
    }

    @Test
    void BTexportToPDF() {
    }


    @Test
    void countOvertimeInWeek() {

        OvertimeInWeekDTO overtime = new OvertimeInWeekDTO("2024-05-12 00:00:00", "2024-05-18 00:00:00", 1);
        List<OvertimeApprovalDTO> list = approvalService.findOInWeekByEmployeeId(overtime);

        Assertions.assertDoesNotThrow(
                () -> approvalService.countOvertimeInWeek(list)
        );
    }
}
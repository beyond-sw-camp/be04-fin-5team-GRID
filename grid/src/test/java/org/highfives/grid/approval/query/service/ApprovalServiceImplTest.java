package org.highfives.grid.approval.query.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApprovalServiceImplTest {

    @Autowired
    private ApprovalServiceImpl approvalService;

    @DisplayName("출장 결재 전체 조회")
    @Test
    void findAllBTApproval() {

    }

    @Test
    void findBTApprovalById() {
    }


    @Test
    void BTexportToPDF() {
    }


    @Test
    void countOvertimeInWeek() {
    }
}
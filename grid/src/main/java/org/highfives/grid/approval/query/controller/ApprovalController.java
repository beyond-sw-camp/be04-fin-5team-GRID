package org.highfives.grid.approval.query.controller;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.query.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "QueryApprovalController")
@RequestMapping("/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/bt-pdf/{approvalId}")
    public void downloadBTApprovalPDF(Model model, @PathVariable int approvalId) {

        BTApprovalDTO btApproval = approvalService.findBTApprovalById(approvalId);
        if (btApproval != null) approvalService.BTexportToPDF(btApproval, "business_trip_approval.pdf");  // 생성 일시를 포함한 형태로 제목 수정
    }


}

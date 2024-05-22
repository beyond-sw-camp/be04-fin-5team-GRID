package org.highfives.grid.approval.query.controller;

import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.query.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final ApprovalService approvalService;

    @Autowired
    public PdfController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/pdf")
    public void pdf(Model model){

        BTApproval btApproval = approvalService.findBTApprovalById(1);

        approvalService.exportToPDF(btApproval, "bt.pdf");
    }
}

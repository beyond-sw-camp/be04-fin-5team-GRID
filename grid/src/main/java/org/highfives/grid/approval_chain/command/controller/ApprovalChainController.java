package org.highfives.grid.approval_chain.command.controller;

import org.highfives.grid.approval_chain.command.service.ApprovalChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "CommandApprovalChainController")
@RequestMapping("/approval-chain")
public class ApprovalChainController {

    private final ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainController(ApprovalChainService approvalChainService) {
        this.approvalChainService = approvalChainService;
    }

    // body에 담는 post 메소드로 추후 변경?
    @GetMapping("add/{typeId}/{approvalId}/{employeeId}")
    public void addBTApprovalChain(@PathVariable int typeId, @PathVariable int approvalId, @PathVariable int employeeId) {

        approvalChainService.addBTApprovalChain(typeId, approvalId, employeeId);
    }
}

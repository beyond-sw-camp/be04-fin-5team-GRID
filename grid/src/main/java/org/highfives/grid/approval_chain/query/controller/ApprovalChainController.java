package org.highfives.grid.approval_chain.query.controller;

import org.highfives.grid.approval_chain.query.service.ApprovalChainService;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/approval-chain")
public class ApprovalChainController {

    private final ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainController(ApprovalChainService approvalChainService) {
        this.approvalChainService = approvalChainService;
    }


    @GetMapping("/{approvalId}")
    public ResponseEntity<List<ApprovalChainDTO>> findChainListByApprovalId(@PathVariable int approvalId) {

        List<ApprovalChainDTO> approvalChainDTOList = approvalChainService.findChainListByApprovalId(approvalId);

        return ResponseEntity.status(HttpStatus.OK).body(approvalChainDTOList);
    }
}

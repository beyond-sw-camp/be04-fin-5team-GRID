package org.highfives.grid.approval_chain.command.controller;

import org.highfives.grid.approval_chain.command.service.ApprovalChainService;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.command.vo.ResAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "CommandApprovalChainController")
@RequestMapping("/approval-chain")
public class ApprovalChainController {

    private final ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainController(ApprovalChainService approvalChainService) {
        this.approvalChainService = approvalChainService;
    }

    @PostMapping("/bt")
    public ResponseEntity<ResAddApprovalChainVO> addBTApprovalChain(@RequestBody ReqAddApprovalChainVO btChainVO) {

        List<BTApprovalChainDTO> result = approvalChainService.addBTApprovalChain(btChainVO);

        ResAddApprovalChainVO response = new ResAddApprovalChainVO(
            201, "Success to add new business trip approval chain.", "", result
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

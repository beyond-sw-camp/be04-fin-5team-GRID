package org.highfives.grid.approval_chain.query.controller;

import org.highfives.grid.approval_chain.command.vo.ResApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.ChainDTO;
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

@RestController(value = "QueryApprovalChainController")
@RequestMapping("/approval-chain")
public class ApprovalChainController {

    private final ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainController(ApprovalChainService approvalChainService) {
        this.approvalChainService = approvalChainService;
    }

    // 직원 부분에서 받은 배열 변형에 사용
    @GetMapping("/{typeId}")
    public ResponseEntity<List<ApprovalChainDTO>> findChainListByTypeId(@PathVariable int typeId) {

        List<ApprovalChainDTO> approvalChainDTOList = approvalChainService.findChainListByTypeId(typeId);

        return ResponseEntity.status(HttpStatus.OK).body(approvalChainDTOList);
    }

    @GetMapping("list/{typeId}/{approvalId}")
    public ResponseEntity<ResApprovalChainVO> findChainByApprovalId(@PathVariable int typeId, @PathVariable int approvalId) {

        List<ChainDTO> result = approvalChainService.findChainByApprovalId(typeId, approvalId);

        ResApprovalChainVO response = ResApprovalChainVO.builder()
                .statusCode(200)
                .message("결재 라인 상세 조회 성공")
                .href("")
                .chainResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

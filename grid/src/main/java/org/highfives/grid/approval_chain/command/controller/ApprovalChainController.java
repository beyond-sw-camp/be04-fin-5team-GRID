package org.highfives.grid.approval_chain.command.controller;

import org.highfives.grid.approval_chain.command.vo.ChainStatusVO;
import org.highfives.grid.approval_chain.command.vo.CommentVO;
import org.highfives.grid.approval_chain.command.service.ApprovalChainService;
import org.highfives.grid.approval_chain.command.vo.ResApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.RWApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.VApprovalChainDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/bt")
    public ResponseEntity<ResApprovalChainVO> addBTApprovalComment(@RequestBody CommentVO commentVO) {

        BTApprovalChainDTO result = approvalChainService.addBTApprovalComment(commentVO);

        ResApprovalChainVO response = ResApprovalChainVO.builder()
                .statusCode(200)
                .message("출장 결재 댓글 생성 성공")
                .href("")
                .btChainResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/overtime")
    public ResponseEntity<ResApprovalChainVO> addOApprovalComment(@RequestBody CommentVO commentVO) {

        OApprovalChainDTO result = approvalChainService.addOApprovalComment(commentVO);

        ResApprovalChainVO response = ResApprovalChainVO.builder()
                .statusCode(200)
                .message("시간 외 근무 결재 댓글 생성 성공")
                .href("")
                .oChainResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/rw")
    public ResponseEntity<ResApprovalChainVO> addRWApprovalComment(@RequestBody CommentVO commentVO) {

        RWApprovalChainDTO result = approvalChainService.addRWApprovalComment(commentVO);

        ResApprovalChainVO response = ResApprovalChainVO.builder()
                .statusCode(200)
                .message("단축 근무 결재 댓글 생성 성공")
                .href("")
                .rwChainResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/vacation")
    public ResponseEntity<ResApprovalChainVO> addVacationComment(@RequestBody CommentVO commentVO) {

        VApprovalChainDTO result = approvalChainService.addVApprovalComment(commentVO);

        ResApprovalChainVO response = ResApprovalChainVO.builder()
                .statusCode(200)
                .message("휴가 근무 결재 댓글 생성 성공")
                .href("")
                .vChainResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/status")
    public ResponseEntity<ResApprovalChainVO> modifyChainStatus(@RequestBody ChainStatusVO chainStatusVO) {

        ResApprovalChainVO response = new ResApprovalChainVO();

        switch (chainStatusVO.getTypeId()) {
            case 1:
                BTApprovalChainDTO btResult = approvalChainService.modifyBTChainStatus(chainStatusVO);

                response = ResApprovalChainVO.builder()
                        .statusCode(200)
                        .message("출장 결재 성공")
                        .href("")
                        .btChainResult(btResult)
                        .build();
                break;

            case 2:
                OApprovalChainDTO oResult = approvalChainService.modifyOChainStatus(chainStatusVO);

                response = ResApprovalChainVO.builder()
                        .statusCode(200)
                        .message("시간 외 근무 결재 성공")
                        .href("")
                        .oChainResult(oResult)
                        .build();
                break;

            case 3:
                RWApprovalChainDTO rwResult = approvalChainService.modifyRWChainStatus(chainStatusVO);

                response = ResApprovalChainVO.builder()
                        .statusCode(200)
                        .message("단축 근무 결재 성공")
                        .href("")
                        .rwChainResult(rwResult)
                        .build();
                break;

            case 4:
                VApprovalChainDTO vResult = approvalChainService.modifyVChainStatus(chainStatusVO);

                response = ResApprovalChainVO.builder()
                        .statusCode(200)
                        .message("휴가 결재 성공")
                        .href("")
                        .vChainResult(vResult)
                        .build();
                break;
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

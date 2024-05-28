package org.highfives.grid.work.query.controller;

import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.work.query.service.RwApprovalService;
import org.highfives.grid.work.query.vo.ResponseRwListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rw")
public class RwApprovalController {

    private final RwApprovalService rwApprovalService;

    @Autowired
    public RwApprovalController(RwApprovalService rwApprovalService) {
        this.rwApprovalService = rwApprovalService;
    }

    /* 설명. 승인된 근무 단축 전체 조회 */
    @GetMapping("/all")
    public ResponseEntity<ResponseRwListVO> findAllRw() {
        List<RWApprovalDTO> finRwList = rwApprovalService.findAllRw();

        return getResponseRwListVOResponseEntity(finRwList);
    }

    /* 설명. 승인된 근무 단축 직원별 조회 */
    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseRwListVO> findRwByEmployeeId(@PathVariable int employeeId) {
        List<RWApprovalDTO> finRwList = rwApprovalService.findRwByEmployeeId(employeeId);

        return getResponseRwListVOResponseEntity(finRwList);
    }

    private ResponseEntity<ResponseRwListVO> getResponseRwListVOResponseEntity(List<RWApprovalDTO> finRwList) {
        ResponseRwListVO response;

        if(!finRwList.isEmpty()){
            response = ResponseRwListVO.builder()
                    .message("조회 성공")
                    .rwList(finRwList)
                    .build();
        } else {
            response = ResponseRwListVO.builder()
                    .message("조회 된 값이 없습니다.")
                    .rwList(finRwList)
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

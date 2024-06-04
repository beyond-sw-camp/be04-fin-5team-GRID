package org.highfives.grid.department.command.controller;

import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.highfives.grid.department.command.service.DepartmentService;
import org.highfives.grid.department.command.vo.ResponseDepartmentListVO;
import org.highfives.grid.department.command.vo.ResponseDepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController(value = "CommandDepartmentController")
@RequestMapping("/department")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /* 설명. 부서 정보 조회 기능 */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDepartmentVO> findDepartmentById(@PathVariable int id) {

        // 해당 부서 정보 반환
        DepartmentDTO departmentInfo = departmentService.findDepartmentById(id);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .result(departmentInfo)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }

    /* 설명. 부서 등록 기능 */
    @PostMapping
    public ResponseEntity<ResponseDepartmentVO> registDepartment(@RequestBody DepartmentDTO departmentDTO) {

        DepartmentDTO departmentList = departmentService.registDepartment(departmentDTO);
        log.info("departmentList={}", departmentList);
        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(201)
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDepartmentVO);
    }

    /* 설명. 부서 정보 수정 기능*/
    @PutMapping
    public ResponseEntity<ResponseDepartmentVO> modifyDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO departmentList = departmentService.modifyDepartment(departmentDTO);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }


    /* 설명. 부서 전체 정보 수정 기능 */
    @PutMapping("/list")
    public ResponseEntity<ResponseDepartmentListVO> modifyAllDepartment(@RequestBody List<DepartmentDTO> departmentDTO) {
        List<DepartmentDTO> departmentList = departmentService.modifyAllDepartment(departmentDTO);

        ResponseDepartmentListVO responseDepartmentVO = ResponseDepartmentListVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }


    /* 설명. 부서 삭제 기능 */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDepartmentVO> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }


    /* 설명. 상태값만 바꾸는 수정 */
    @PutMapping("/status")
    public ResponseEntity<ResponseDepartmentListVO> modifyDepartmentStatus(@RequestBody List<DepartmentDTO> departmentDTO) {

        List<DepartmentDTO> departmentList = departmentService.modifyDepartmentStatus(departmentDTO);

        ResponseDepartmentListVO responseDepartmentVO = ResponseDepartmentListVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }


    @PutMapping("/department-leader")
    public ResponseEntity<ResponseDepartmentVO> modifyDepartmentLeader(@RequestBody DepartmentDTO departmentDTO) {

        DepartmentDTO departmentLeader = departmentService.modifyDepartmentLeader(departmentDTO);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .result(departmentLeader)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }

}

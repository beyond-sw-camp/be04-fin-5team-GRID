package org.highfives.grid.department.query.controller;



import org.highfives.grid.department.query.dto.DepartmentDTO;
import org.highfives.grid.department.query.service.DepartmentService;
import org.highfives.grid.department.query.vo.ResponseDepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("mybatis")
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }




    /* 설명. 부서 전체 조회 */
    @GetMapping("/find-all")
    public ResponseEntity<ResponseDepartmentVO> findAllDepartment() {

        List<DepartmentDTO> departmentList = departmentService.findAllDepartment();

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .statusCode(200)
                .message("success")
                .href("/find-all")
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }



}

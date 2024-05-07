package org.highfives.grid.department.controller;

import org.highfives.grid.department.dto.DepartmentDTO;
import org.highfives.grid.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /* 설명. 부서 정보 조회 기능 */
    @GetMapping("/find/{id}")
    public ResponseEntity<DepartmentDTO> findDepartmentById(@PathVariable int id) {

        // 해당 부서 정보 반환
        DepartmentDTO departmentInfo = departmentService.findDepartmentById(id);

        return ResponseEntity.ok().body(departmentInfo);
    }

    /* 설명. 부서 전체 조회 */
    @GetMapping("/find-all")
    public ResponseEntity<List<DepartmentDTO>> findAllDepartment() {

        List<DepartmentDTO> departmentList = departmentService.findAllDepartment();

        return ResponseEntity.ok().body(departmentList);
    }

}

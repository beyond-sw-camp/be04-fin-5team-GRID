package org.highfives.grid.department.command.controller;

import org.highfives.grid.department.dto.DepartmentDTO;
import org.highfives.grid.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("jpa")
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

    /* 설명. 부서 등록 기능 */
    @PostMapping("/regist")
    public ResponseEntity<DepartmentDTO> registDepartment(@RequestBody DepartmentDTO departmentDTO) {

        DepartmentDTO departmentList = departmentService.registDepartment(departmentDTO);

        return ResponseEntity.ok().body(departmentList);
    }


}

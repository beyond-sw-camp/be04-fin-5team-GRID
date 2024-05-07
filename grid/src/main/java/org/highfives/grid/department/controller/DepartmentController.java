package org.highfives.grid.department.controller;

import org.highfives.grid.department.dto.DepartmentDTO;
import org.highfives.grid.department.entity.Department;
import org.highfives.grid.department.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }



    @GetMapping("/find/{id}")
    public ResponseEntity<DepartmentDTO> findDepartmentById(@PathVariable int id) {

        // 해당 부서 정보 반환
        DepartmentDTO departmentDTO = departmentService.findDepartmentById(id);

        return ResponseEntity.ok().body(departmentDTO);
    }


}

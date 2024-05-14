//package org.highfives.grid.department.query.controller;
//
//import org.highfives.grid.department.dto.DepartmentDTO;
//import org.highfives.grid.department.service.DepartmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/department")
//public class DepartmentController {
//
//    private final DepartmentService departmentService;
//
//    @Autowired
//    public DepartmentController(DepartmentService departmentService) {
//        this.departmentService = departmentService;
//    }
//
//
//    /* 설명. 부서 전체 조회 */
//    @GetMapping("/find-all")
//    public ResponseEntity<List<DepartmentDTO>> findAllDepartment() {
//
//        List<DepartmentDTO> departmentList = departmentService.findAllDepartment();
//
//        return ResponseEntity.ok().body(departmentList);
//    }
//
//
//
//}

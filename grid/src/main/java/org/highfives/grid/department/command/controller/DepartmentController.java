package org.highfives.grid.department.command.controller;

import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.highfives.grid.department.command.service.CommandDepartmentService;
import org.highfives.grid.department.command.vo.ResponseDepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("jpa")
@RequestMapping("/department")
public class DepartmentController {

    private final CommandDepartmentService commandDepartmentService;

    @Autowired
    public DepartmentController(CommandDepartmentService commandDepartmentService) {
        this.commandDepartmentService = commandDepartmentService;
    }

    /* 설명. 부서 정보 조회 기능 */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDepartmentVO> findDepartmentById(@PathVariable int id) {

        // 해당 부서 정보 반환
        DepartmentDTO departmentInfo = commandDepartmentService.findDepartmentById(id);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .result(departmentInfo)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }

    /* 설명. 부서 등록 기능 */
    @PostMapping("/add")
    public ResponseEntity<ResponseDepartmentVO> registDepartment(@RequestBody DepartmentDTO departmentDTO) {

        DepartmentDTO departmentList = commandDepartmentService.registDepartment(departmentDTO);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(201)
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDepartmentVO);
    }

    /* 설명. 부서 정보 수정 기능*/
    @PutMapping("/update")
    public ResponseEntity<ResponseDepartmentVO> modifyDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO departmentList = commandDepartmentService.modifyDepartment(departmentDTO);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(201)
                .result(departmentList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }

    /* 설명. 부서 삭제 기능 */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDepartmentVO> deleteDepartment(@PathVariable int id) {
        commandDepartmentService.deleteDepartment(id);

        ResponseDepartmentVO responseDepartmentVO = ResponseDepartmentVO.builder()
                .message("success")
                .href("/{id}")
                .statusCode(200)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDepartmentVO);
    }

}

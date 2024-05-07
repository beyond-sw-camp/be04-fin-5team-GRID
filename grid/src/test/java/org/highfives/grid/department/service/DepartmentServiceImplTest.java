package org.highfives.grid.department.service;

import org.highfives.grid.department.dto.DepartmentDTO;
import org.highfives.grid.department.entity.Department;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DepartmentServiceImplTest {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentServiceImplTest(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }



    @Test
    @DisplayName("부서 조회")
    void findDepartmentById() {
        // Given
        int id = 1;

        // When
        DepartmentDTO departmentById = departmentService.findDepartmentById(id);

        // Then
        assertThat(departmentById.getId()).isEqualTo(id);

    }

    @Test
    @DisplayName("부서 전체 조회")
    void findAllDepartment() {

        // Given

        // When
        List<DepartmentDTO> departmentDTOList = departmentService.findAllDepartment();

        // Then
        assertThat(departmentDTOList).isNotNull();
    }

    @Test
    @DisplayName("부서 등록")
    @Transactional
    void registDepartment() {

        // Given
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("테스트");

        // When
        DepartmentDTO departmentDTOList = departmentService.registDepartment(departmentDTO);

        // Then

        assertThat(departmentDTOList.getName()).isEqualTo("테스트");
    }
}
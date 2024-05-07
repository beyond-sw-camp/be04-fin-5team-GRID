package org.highfives.grid.department.controller;

import org.assertj.core.api.Assertions;
import org.highfives.grid.department.dto.DepartmentDTO;
import org.highfives.grid.department.service.DepartmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class DepartmentControllerTest {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentControllerTest(DepartmentService departmentService) {
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
        System.out.println(departmentById.getId());
        assertThat(departmentById.getId()).isEqualTo(id);

    }
}
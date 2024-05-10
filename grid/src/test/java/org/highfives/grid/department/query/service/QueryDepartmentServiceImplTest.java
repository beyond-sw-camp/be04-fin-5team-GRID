package org.highfives.grid.department.query.service;

import org.highfives.grid.department.query.dto.DepartmentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryDepartmentServiceImplTest {

    private final QueryDepartmentService queryDepartmentService;

    @Autowired
    public QueryDepartmentServiceImplTest(QueryDepartmentService queryDepartmentService) {
        this.queryDepartmentService = queryDepartmentService;
    }

    @Test
    @DisplayName("부서 전체 조회")
    void findAllDepartment() {

        // Given

        // When
        List<DepartmentDTO> departmentDTOList = queryDepartmentService.findAllDepartment();

        // Then
        assertThat(departmentDTOList).isNotNull();
    }
}
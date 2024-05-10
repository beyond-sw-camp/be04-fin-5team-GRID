package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.service.CommandDepartmentService;
import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommandDepartmentServiceImplTest {

    private final CommandDepartmentService commandDepartmentService;

    @Autowired
    public CommandDepartmentServiceImplTest(CommandDepartmentService commandDepartmentService) {
        this.commandDepartmentService = commandDepartmentService;
    }



    @Test
    @DisplayName("부서 조회")
    void findDepartmentById() {
        // Given
        int id = 1;

        // When
        DepartmentDTO departmentById = commandDepartmentService.findDepartmentById(id);

        // Then
        assertThat(departmentById.getId()).isEqualTo(id);

    }



    @Test
    @DisplayName("부서 등록")
    @Transactional
    void registDepartment() {

        // Given
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("테스트");

        // When
        DepartmentDTO departmentDTOList = commandDepartmentService.registDepartment(departmentDTO);

        // Then

        assertThat(departmentDTOList.getName()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("부서 정보 수정")
    @Transactional
    void modifyDepartment() {

        // Given
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .id(8)
                .name("update")
                .highDepartment("update2")
                .departmentStatus("N")
                .leaderId(1)
                .build();

        // When
        DepartmentDTO departmentDTOList = commandDepartmentService.modifyDepartment(departmentDTO);

        // Then

        assertThat(departmentDTOList.getName()).isEqualTo("update");
        assertThat(departmentDTOList.getHighDepartment()).isEqualTo("update2");
        assertThat(departmentDTOList.getLeaderId()).isEqualTo(1);

    }

    @Test
    @DisplayName("부서 삭제")
    @Transactional
    void deleteDepartment() {

        // Given
        int id = 1;

        // When
        commandDepartmentService.deleteDepartment(id);

//        DepartmentDTO departmentById = departmentService.findDepartmentById(id);
        // Then

    }


}
package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("부서 등록")
    @Transactional
    void registDepartment() {

        // Given
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentName("테스트");
        departmentDTO.setDepartmentCode("0100");

        // When
        DepartmentDTO departmentDTOList = departmentService.registDepartment(departmentDTO);

        // Then

        assertThat(departmentDTOList.getDepartmentName()).isEqualTo("테스트");
        assertThat(departmentDTOList.getDepartmentCode()).isEqualTo("0100");
    }

    @Test
    @DisplayName("부서 정보 수정")
    @Transactional
    void modifyDepartment() {

        // Given
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .id(8)
                .departmentName("update")
                .highDepartment("update2")
                .departmentStatus("N")
                .leaderId(1)
                .build();

        // When
        DepartmentDTO departmentDTOList = departmentService.modifyDepartment(departmentDTO);

        // Then

        assertThat(departmentDTOList.getDepartmentName()).isEqualTo("update");
        assertThat(departmentDTOList.getHighDepartment()).isEqualTo("update2");
        assertThat(departmentDTOList.getLeaderId()).isEqualTo(1);

    }

    @Test
    @DisplayName("부서 전체 정보 수정")
    @Transactional
    void modifyAllDepartment() {

        // Given
        List<DepartmentDTO> updateList = new ArrayList<>();

        DepartmentDTO departmentDTO1 = DepartmentDTO.builder()
                .id(8)
                .departmentName("update8")
                .highDepartment("update2")
                .departmentStatus("N")
                .leaderId(1)
                .departmentCode("0010")
                .build();

        DepartmentDTO departmentDTO2 = DepartmentDTO.builder()
                .id(6)
                .departmentName("update6")
                .highDepartment("update2")
                .departmentStatus("N")
                .leaderId(1)
                .departmentCode("0020")
                .build();
        DepartmentDTO departmentDTO3 = DepartmentDTO.builder()
                .id(7)
                .departmentName("update7")
                .highDepartment("update2")
                .departmentStatus("N")
                .leaderId(1)
                .departmentCode("0030")
                .build();
        updateList.add(departmentDTO1);
        updateList.add(departmentDTO2);
        updateList.add(departmentDTO3);

        // When
        List<DepartmentDTO> departmentDTOList = departmentService.modifyAllDepartment(updateList);

        // Then
        assertThat(departmentDTOList).hasSize(updateList.size());

        for (int i = 0; i < updateList.size(); i++) {
            DepartmentDTO updatedDepartment = departmentDTOList.get(i);
            DepartmentDTO expectedDepartment = updateList.get(i);

            assertThat(updatedDepartment.getId()).isEqualTo(expectedDepartment.getId());
            assertThat(updatedDepartment.getDepartmentName()).isEqualTo(expectedDepartment.getDepartmentName());
            assertThat(updatedDepartment.getHighDepartment()).isEqualTo(expectedDepartment.getHighDepartment());
            assertThat(updatedDepartment.getDepartmentStatus()).isEqualTo(expectedDepartment.getDepartmentStatus());
            assertThat(updatedDepartment.getLeaderId()).isEqualTo(expectedDepartment.getLeaderId());
            assertThat(updatedDepartment.getDepartmentCode()).isEqualTo(expectedDepartment.getDepartmentCode());
        }

    }

    @Test
    @DisplayName("부서 삭제")
    @Transactional
    void deleteDepartment() {

        // Given
        int id = 1;

        // When
        departmentService.deleteDepartment(id);

//        DepartmentDTO departmentById = departmentService.findDepartmentById(id);
        // Then

    }


}
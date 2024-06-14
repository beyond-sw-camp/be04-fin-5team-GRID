//package org.highfives.grid.department.command.service;
//
//import jakarta.transaction.Transactional;
//import org.highfives.grid.department.command.aggregate.Department;
//import org.highfives.grid.department.command.dto.DepartmentDTO;
//import org.highfives.grid.department.command.repository.DepartmentRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class DepartmentServiceImplTest {
//    private final DepartmentService departmentService;
//    private final DepartmentRepository departmentRepository;
//
//    @Autowired
//    public DepartmentServiceImplTest(DepartmentService departmentService, DepartmentRepository departmentRepository) {
//        this.departmentService = departmentService;
//        this.departmentRepository = departmentRepository;
//    }
//
//
//
//    @Test
//    @DisplayName("부서 조회")
//    void findDepartmentById() {
//        // Given
//        int id = 1;
//
//        // When
//        DepartmentDTO departmentById = departmentService.findDepartmentById(id);
//
//        // Then
//        assertThat(departmentById.getId()).isEqualTo(id);
//
//    }
//
//    @Test
//    @DisplayName("부서 등록")
//    @Transactional
//    void registDepartment() {
//
//        // Given
//        DepartmentDTO departmentDTO = new DepartmentDTO();
//        departmentDTO.setDepartmentName("테스트123122");
//        departmentDTO.setDepartmentCode("010021");
//        departmentDTO.setLeaderId(1);
//
//        // When
//        DepartmentDTO departmentDTOList = departmentService.registDepartment(departmentDTO);
//
//        // Then
//
//        assertThat(departmentDTOList.getDepartmentName()).isEqualTo(departmentDTO.getDepartmentName());
//        assertThat(departmentDTOList.getDepartmentCode()).isEqualTo(departmentDTO.getDepartmentCode());
//    }
//
//
//
//    @Test
//    @DisplayName("부서 전체 정보 수정")
//    @Transactional
//    void modifyAllDepartment() {
//
//        // Given
//        List<DepartmentDTO> updateList = new ArrayList<>();
//
//        DepartmentDTO departmentDTO1 = DepartmentDTO.builder()
//                .id(8)
//                .departmentName("update8")
//                .departmentStatus("N")
//                .leaderId(1)
//                .departmentCode("0010")
//                .build();
//
//        DepartmentDTO departmentDTO2 = DepartmentDTO.builder()
//                .id(6)
//                .departmentName("update6")
//                .departmentStatus("N")
//                .leaderId(1)
//                .departmentCode("0020")
//                .build();
//        DepartmentDTO departmentDTO3 = DepartmentDTO.builder()
//                .id(7)
//                .departmentName("update7")
//                .departmentStatus("N")
//                .leaderId(1)
//                .departmentCode("0030")
//                .build();
//        updateList.add(departmentDTO1);
//        updateList.add(departmentDTO2);
//        updateList.add(departmentDTO3);
//
//        // When
//        List<DepartmentDTO> departmentDTOList = departmentService.modifyAllDepartment(updateList);
//
//        // Then
//        assertThat(departmentDTOList).hasSize(updateList.size());
//
//        for (int i = 0; i < updateList.size(); i++) {
//            DepartmentDTO updatedDepartment = departmentDTOList.get(i);
//            DepartmentDTO expectedDepartment = updateList.get(i);
//
//            assertThat(updatedDepartment.getId()).isEqualTo(expectedDepartment.getId());
//            assertThat(updatedDepartment.getDepartmentName()).isEqualTo(expectedDepartment.getDepartmentName());
//            assertThat(updatedDepartment.getDepartmentStatus()).isEqualTo(expectedDepartment.getDepartmentStatus());
//            assertThat(updatedDepartment.getLeaderId()).isEqualTo(expectedDepartment.getLeaderId());
//            assertThat(updatedDepartment.getDepartmentCode()).isEqualTo(expectedDepartment.getDepartmentCode());
//        }
//
//    }
//
//    @Test
//    @DisplayName("부서 정보 수정")
//    @Transactional
//    void modifyDepartment() {
//
//        // Given
//        DepartmentDTO departmentDTO = DepartmentDTO.builder()
//                .id(8)
//                .departmentName("update")
//                .departmentStatus("N")
//                .leaderId(1)
//                .build();
//
//        // When
//        DepartmentDTO departmentDTOList = departmentService.modifyDepartment(departmentDTO);
//
//        // Then
//
//        assertThat(departmentDTOList.getDepartmentName()).isEqualTo("update");
//        assertThat(departmentDTOList.getLeaderId()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("부서 삭제")
//    @Transactional
//    void deleteDepartment() {
//
//        // Given
//        int id = 1;
//
//        // When
//        departmentService.deleteDepartment(id);
//
//        // Then
//
//    }
//    @Test
//    @DisplayName("부서 활성/비활성화")
//    @Transactional
//    void modifyDepartmentStatus() {
//
//        // Given
//        List<DepartmentDTO> updateList = new ArrayList<>();
//
//        DepartmentDTO departmentDTO1 = DepartmentDTO.builder()
//                .id(8)
//                .departmentName("update8")
//                .departmentStatus("N")
//                .leaderId(1)
//                .departmentCode("0010")
//                .build();
//
//        DepartmentDTO departmentDTO2 = DepartmentDTO.builder()
//                .id(6)
//                .departmentName("update6")
//                .departmentStatus("N")
//                .leaderId(1)
//                .departmentCode("0020")
//                .build();
//
//        updateList.add(departmentDTO1);
//        updateList.add(departmentDTO2);
//
//        // When
//        List<DepartmentDTO> testDataList = departmentService.modifyDepartmentStatus(updateList);
//
//        // Then
//        for (DepartmentDTO testData : testDataList) {
//            assertThat(testData).isNotNull();
//            assertThat(testData.getDepartmentStatus()).isSameAs("Y");
//        }
//
//    }
//
//    @Test
//    @DisplayName("부서장 변경")
//    @Transactional
//    void modifyDepartmentLeader() {
//        // Given
//        DepartmentDTO departmentDTO1 = DepartmentDTO.builder()
//                .id(1)
//                .leaderId(4)
//                .build();
//
//        // When
//        DepartmentDTO testData = departmentService.modifyDepartmentLeader(departmentDTO1);
//
////        Department Data = departmentRepository.findById(departmentDTO1.getId()).orElseThrow(() -> new RuntimeException());
//        // Then
//
//
//        assertThat(testData).isNotNull();
//        assertThat(departmentDTO1.getLeaderId()).isSameAs(testData.getLeaderId());
//    }
//}
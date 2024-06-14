//package org.highfives.grid.department.query.service;
//
//
//import org.highfives.grid.department.query.dto.DepartmentDTO;
//import org.highfives.grid.department.query.dto.TeamDTO;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class TeamServiceImplTest {
//
//
//    private final TeamService teamService;
//
//    @Autowired
//    public TeamServiceImplTest(TeamService teamService) {
//        this.teamService = teamService;
//    }
//
//    @Test
//    @DisplayName("팀 단일 조회")
//    void findTeamById() {
//
//        // Given
//        int id = 1;
//
//        // When
//        TeamDTO teamDTO = teamService.findTeamById(id);
//
//        // Then
//            assertThat(teamDTO).isNotNull();
//            TeamDTO findTest = teamService.findTeamById(teamDTO.getId());
//            assertThat(teamDTO.getTeamName()).isEqualTo(findTest.getTeamName());
//
//    }
//
//    @Test
//    @DisplayName("팀 전체 조회")
//    void findTeamList() {
//
//        // Given
//
//        // When
//        List<TeamDTO> teamDTOList = teamService.findTeamList();
//
//        // Then
//        for (TeamDTO teamDTO : teamDTOList) {
//            assertThat(teamDTO).isNotNull();
//            TeamDTO findTest = teamService.findTeamById(teamDTO.getId());
//            assertThat(teamDTO.getTeamName()).isEqualTo(findTest.getTeamName());
//        }
//    }
//
//    @Test
//    @DisplayName("해당 부서 소속팀 조회")
//    void findSubDepartmentByDepartmentId() {
//        // Given
//        int departmentId= 1;
//
//        // When
//        List<TeamDTO> teamDTOList = teamService.findSubDepartmentByDepartmentId(departmentId);
//
//        // Then
//        for (TeamDTO teamDTO : teamDTOList) {
//            assertThat(teamDTO).isNotNull();
//            assertThat(teamDTO.getDepartmentId()).isEqualTo(departmentId);
//        }
//
//    }
//
//}
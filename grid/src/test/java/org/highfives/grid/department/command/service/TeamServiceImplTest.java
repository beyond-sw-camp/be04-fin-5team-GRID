//package org.highfives.grid.department.command.service;
//
//import jakarta.transaction.Transactional;
//import org.highfives.grid.department.command.aggregate.Team;
//import org.highfives.grid.department.command.aggregate.TeamStatus;
//import org.highfives.grid.department.command.dto.TeamDTO;
//import org.highfives.grid.department.command.repository.TeamRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class TeamServiceImplTest {
//    private final TeamService teamService;
//
//    private final TeamRepository teamRepository;
//
//    @Autowired
//    public TeamServiceImplTest(TeamService teamService, TeamRepository teamRepository) {
//        this.teamService = teamService;
//        this.teamRepository = teamRepository;
//    }
//
//    @Test
//    @DisplayName("팀 등록 기능")
//    @Transactional
//    void addTeam() {
//
//        // Given
//        TeamDTO teamDTO = TeamDTO.builder()
//                .teamName("test")
//                .leaderId(3)
//                .departmentId(1)
//                .build();
//
//        // When
//        TeamDTO addTeam = teamService.addTeam(teamDTO);
//
//        // Then
//        assertThat(addTeam).isNotNull();
//
//    }
//
//    @Test
//    @DisplayName("팀 수정 기능")
//    @Transactional
//    void modifyTeam() {
//
//        // Given
//        TeamDTO teamDTO = TeamDTO.builder()
//                .id(1)
//                .teamName("test")
//                .teamStatus(TeamStatus.N)
//                .leaderId(2)
//                .departmentId(1)
//                .build();
//
//        // When
//        TeamDTO modifyTeam = teamService.modifyTeam(teamDTO);
//
//        // Then
//        assertThat(modifyTeam).isNotNull();
//        assertThat(modifyTeam.getTeamStatus()).isEqualTo(teamDTO.getTeamStatus());
//
//    }
//
//    @Test
//    @DisplayName("팀 삭제 기능")
//    @Transactional
//    void deleteTeam() {
//
//        // Given
//        int id = 1;
//        Team team = teamRepository.findById(id).orElseThrow();
//
//        // When
//        teamService.deleteTeam(id);
//
//        // Then
//        assertThat(team).isNotNull();
//
//    }
//
//    @Test
//    @DisplayName("팀장 변경")
//    @Transactional
//    void modifyTeamLeader() {
//
//        // Given
//        TeamDTO teamDTO = TeamDTO.builder()
//                .id(1)
//                .teamName("test")
//                .teamStatus(TeamStatus.N)
//                .leaderId(3)
//                .departmentId(1)
//                .build();
//
//        // When
//        TeamDTO testData = teamService.modifyTeamLeader(teamDTO);
//
//        // Then
//        assertThat(testData).isNotNull();
//        assertThat(testData.getLeaderId()).isEqualTo(teamDTO.getLeaderId());
//
//    }
//
//    @Test
//    @DisplayName("팀 활성/비활성화")
//    @Transactional
//    void modifyTeamStatus() {
//
//        // Given
//        List<TeamDTO> teamDTOList = new ArrayList<>();
//
//        TeamDTO teamDTO = TeamDTO.builder()
//                .id(1)
//                .teamName("test")
//                .teamStatus(TeamStatus.N)
//                .leaderId(2)
//                .departmentId(1)
//                .build();
//
//        teamDTOList.add(teamDTO);
//
//        // When
//        List<TeamDTO> testDataList = teamService.modifyTeamStatus(teamDTOList);
//
//        // Then
//        for (TeamDTO testData : testDataList) {
//            assertThat(testData).isNotNull();
//            assertThat(testData.getLeaderId()).isNotSameAs(teamDTO.getLeaderId());
//        }
//
//
//    }
//}
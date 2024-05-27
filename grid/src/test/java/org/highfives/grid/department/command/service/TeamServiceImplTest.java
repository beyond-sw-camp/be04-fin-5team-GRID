package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.aggregate.Team;
import org.highfives.grid.department.command.aggregate.TeamStatus;
import org.highfives.grid.department.command.dto.TeamDTO;
import org.highfives.grid.department.command.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamServiceImplTest {

    private final TeamService teamService;

    private final TeamRepository teamRepository;

    @Autowired
    public TeamServiceImplTest(TeamService teamService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.teamRepository = teamRepository;
    }



    @Test
    @DisplayName("팀 등록 기능")
    @Transactional
    void addTeam() {

        // Given
        TeamDTO teamDTO = TeamDTO.builder()
                .teamName("test")
                .leaderId(1)
                .departmentId(1)
                .build();

        // When
        TeamDTO addTeam = teamService.addTeam(teamDTO);

        // Then
        assertThat(addTeam).isNotNull();

    }

    @Test
    @DisplayName("팀 수정 기능")
    @Transactional
    void modifyTeam() {

        // Given
        TeamDTO teamDTO = TeamDTO.builder()
                .id(1)
                .teamName("test")
                .teamStatus(TeamStatus.N)
                .leaderId(2)
                .departmentId(1)
                .build();

        // When
        TeamDTO modifyTeam = teamService.modifyTeam(teamDTO);

        // Then
        assertThat(modifyTeam).isNotNull();
        assertThat(modifyTeam.getTeamStatus()).isEqualTo(teamDTO.getTeamStatus());

    }

    @Test
    @DisplayName("팀 삭제 기능")
    @Transactional
    void deleteTeam() {

        // Given
        int id = 1;
        Team team = teamRepository.findById(id).orElseThrow();

        // When
        teamService.deleteTeam(id);

        // Then
        assertThat(team).isNotNull();

    }

}
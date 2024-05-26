package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.aggregate.TeamStatus;
import org.highfives.grid.department.command.dto.TeamDTO;
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

    @Autowired
    public TeamServiceImplTest(TeamService teamService) {
        this.teamService = teamService;
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

}
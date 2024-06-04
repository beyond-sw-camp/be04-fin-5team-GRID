package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.TeamDTO;

import java.util.List;

public interface TeamService {
    TeamDTO addTeam(TeamDTO teamDTO);

    TeamDTO modifyTeam(TeamDTO teamDTO);

    void deleteTeam(int id);

    TeamDTO modifyTeamLeader(TeamDTO teamDTO);

    List<TeamDTO> modifyTeamStatus(List<TeamDTO> teamDTO);
}

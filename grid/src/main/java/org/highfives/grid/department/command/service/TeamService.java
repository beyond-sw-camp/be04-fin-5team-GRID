package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.TeamDTO;

public interface TeamService {
    TeamDTO addTeam(TeamDTO teamDTO);

    TeamDTO modifyTeam(TeamDTO teamDTO);

    void deleteTeam(int id);

}

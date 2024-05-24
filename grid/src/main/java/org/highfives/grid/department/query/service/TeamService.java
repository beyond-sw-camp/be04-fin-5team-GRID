package org.highfives.grid.department.query.service;

import org.highfives.grid.department.query.dto.TeamDTO;

public interface TeamService {
    TeamDTO findTeamById(int id);
}

package org.highfives.grid.department.query.service;

import org.highfives.grid.department.query.dto.TeamDTO;

import java.util.List;

public interface TeamService {
    TeamDTO findTeamById(int id);

    List<TeamDTO> findTeamList();

    List<TeamDTO> findSubDepartmentByDepartmentId(int id);
}

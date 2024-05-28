package org.highfives.grid.department.query.service;

import org.highfives.grid.department.query.aggregate.Team;
import org.highfives.grid.department.query.dto.TeamDTO;
import org.highfives.grid.department.query.repository.TeamMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "queryTeamService")
public class TeamServiceImpl implements TeamService {

    private final TeamMapper teamMapper;
    private final ModelMapper mapper;


    @Autowired
    public TeamServiceImpl(TeamMapper teamMapper, ModelMapper mapper) {
        this.teamMapper = teamMapper;
        this.mapper = mapper;
    }

    @Override
    public TeamDTO findTeamById(int id) {

        Team team = teamMapper.findTeamById(id);

        return mapper.map(team, TeamDTO.class);
    }

    @Override
    public List<TeamDTO> findTeamList() {

        List<Team> teamList = teamMapper.findTeamList();

        List<TeamDTO> teamDTOList = new ArrayList<>();

        for (Team team : teamList) {
            teamDTOList.add(mapper.map(team, TeamDTO.class));
        }

        return teamDTOList;

    }

    @Override
    public List<TeamDTO> findSubDepartmentByDepartmentId(int id) {

        List<Team> teamList = teamMapper.findSubDepartmentByDepartmentId(id);

        List<TeamDTO> teamDTOList = new ArrayList<>();

        for (Team team : teamList) {

            teamDTOList.add(mapper.map(team, TeamDTO.class));

        }
        return teamDTOList;

    }
}

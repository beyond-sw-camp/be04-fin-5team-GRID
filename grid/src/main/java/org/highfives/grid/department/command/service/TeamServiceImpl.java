package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.aggregate.Team;
import org.highfives.grid.department.command.aggregate.TeamStatus;
import org.highfives.grid.department.command.dto.TeamDTO;
import org.highfives.grid.department.command.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "commandTeamService")
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final ModelMapper mapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper mapper) {
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    @Override
    public TeamDTO addTeam(TeamDTO teamDTO) {

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);


        Team team = Team.builder()
                .teamName(teamDTO.getTeamName())
                .teamStatus(TeamStatus.Y)
                .startTime(formattedDate)
                .leaderId(teamDTO.getLeaderId())
                .departmentId(teamDTO.getDepartmentId())
                .build();

        teamRepository.save(team);


        return mapper.map(team, TeamDTO.class);
    }
}

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
                .sequence(teamDTO.getSequence())
                .build();

        teamRepository.save(team);


        return mapper.map(team, TeamDTO.class);
    }

    @Override
    public TeamDTO modifyTeam(TeamDTO teamDTO) {
        Team currentTeamData = teamRepository.findById(teamDTO.getId()).orElseThrow(() -> new IllegalArgumentException("값이 없습니다."));

        if (teamDTO.getTeamStatus() == TeamStatus.N) {

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            Team team = Team.builder()
                    .id(teamDTO.getId())
                    .teamName(teamDTO.getTeamName())
                    .teamStatus(teamDTO.getTeamStatus())
                    .startTime(currentTeamData.getStartTime())
                    .endTime(formattedDate)
                    .leaderId(teamDTO.getLeaderId())
                    .departmentId(teamDTO.getDepartmentId())
                    .sequence(teamDTO.getSequence())
                    .build();

            teamRepository.save(team);

            return mapper.map(team, TeamDTO.class);

        }

        Team team = Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .teamStatus(currentTeamData.getTeamStatus())
                .startTime(currentTeamData.getStartTime())
                .leaderId(teamDTO.getLeaderId())
                .departmentId(teamDTO.getDepartmentId())
                .sequence(teamDTO.getSequence())
                .build();

        teamRepository.save(team);

        return mapper.map(team, TeamDTO.class);
    }

    @Override
    public void deleteTeam(int id) {

        teamRepository.deleteById(id);

    }
}

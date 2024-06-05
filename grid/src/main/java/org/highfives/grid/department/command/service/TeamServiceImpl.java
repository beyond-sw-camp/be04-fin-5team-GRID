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
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public TeamDTO modifyTeamLeader(TeamDTO teamDTO) {
        Team currentTeamData = teamRepository.findById(teamDTO.getId()).orElseThrow(() -> new IllegalArgumentException("값이 없습니다."));

            Team team = Team.builder()
                    .id(teamDTO.getId())
                    .teamName(currentTeamData.getTeamName())
                    .teamStatus(currentTeamData.getTeamStatus())
                    .startTime(currentTeamData.getStartTime())
                    .endTime(currentTeamData.getEndTime())
                    .memberCnt(currentTeamData.getMemberCnt())
                    .leaderId(teamDTO.getLeaderId())
                    .departmentId(currentTeamData.getDepartmentId())
                    .sequence(currentTeamData.getSequence())
                    .build();

            teamRepository.save(team);

            return mapper.map(team, TeamDTO.class);

    }

    @Override
    public List<TeamDTO> modifyTeamStatus(List<TeamDTO> teamDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<TeamDTO> updatedTeams = teamDTO.stream().map(update -> {
            Team currentTeamData = teamRepository.findById(update.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

            TeamStatus newStatus = (update.getTeamStatus() == TeamStatus.Y) ? TeamStatus.N : TeamStatus.Y;
            String endTime = TeamStatus.Y.equals(newStatus) ? null : dateFormat.format(new Date());

            Team team = Team.builder()
                    .id(update.getId())
                    .memberCnt(currentTeamData.getMemberCnt())
                    .teamName(currentTeamData.getTeamName())
                    .teamStatus(newStatus)
                    .startTime(currentTeamData.getStartTime())
                    .endTime(endTime)
                    .leaderId(currentTeamData.getLeaderId())
                    .departmentId(currentTeamData.getDepartmentId())
                    .sequence(currentTeamData.getSequence())
                    .build();

            teamRepository.save(team);
            return mapper.map(team, TeamDTO.class);
        }).collect(Collectors.toList());

        return updatedTeams;
    }
}

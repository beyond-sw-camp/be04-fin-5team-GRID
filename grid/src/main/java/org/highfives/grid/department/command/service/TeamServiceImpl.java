package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.aggregate.Team;
import org.highfives.grid.department.command.aggregate.TeamStatus;
import org.highfives.grid.department.command.dto.TeamDTO;
import org.highfives.grid.department.command.repository.DepartmentRepository;
import org.highfives.grid.department.command.repository.TeamRepository;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.repository.UserRepository;
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

    private final DepartmentRepository departmentRepository;

    private final ModelMapper mapper;

    private final UserRepository userRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, DepartmentRepository departmentRepository, ModelMapper mapper, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }



    @Override
    public TeamDTO addTeam(TeamDTO teamDTO) {

        // 사용자가 이미 부서장인지 확인
        boolean isDepartmentLeader = departmentRepository.existsByLeaderId(teamDTO.getLeaderId());
        if (isDepartmentLeader) {
            throw new IllegalArgumentException("해당 사용자는 이미 부서장으로 등록되어 있습니다.");
        }

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        // 새로운 팀 생성
        Team team = Team.builder()
                .teamName(teamDTO.getTeamName())
                .teamStatus(TeamStatus.Y)
                .startTime(formattedDate)
                .leaderId(teamDTO.getLeaderId())
                .departmentId(teamDTO.getDepartmentId())
                .sequence(teamDTO.getSequence())
                .build();

        teamRepository.save(team);

        // 팀장 정보 업데이트
        Employee leader = userRepository.findById(teamDTO.getLeaderId()).orElseThrow(() -> new RuntimeException("리더를 찾을 수 없습니다."));
        leader.setDepartmentId(teamDTO.getDepartmentId());
        leader.setTeamId(team.getId());
        leader.setDutiesId(3);
        userRepository.save(leader);

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

        // 사용자가 이미 부서장인지 확인
        boolean isDepartmentLeader = departmentRepository.existsByLeaderId(teamDTO.getLeaderId());
        if (isDepartmentLeader) {
            throw new IllegalArgumentException("해당 사용자는 이미 부서장으로 등록되어 있습니다.");
        }

        // 현재 팀 정보 가져오기
        Team currentTeamData = teamRepository.findById(teamDTO.getId()).orElseThrow(() -> new IllegalArgumentException("값이 없습니다."));

        // 기존 팀장의 정보를 가져와서 null 처리 (이전 팀에서 제거)
        Employee previousLeader = userRepository.findById(currentTeamData.getLeaderId()).orElse(null);
        if (previousLeader != null) {
            previousLeader.setDutiesId(4);
            userRepository.save(previousLeader);
        }

        // 새로운 팀장 정보 업데이트
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

        // 새로운 팀장의 정보를 업데이트
        Employee newLeader = userRepository.findById(teamDTO.getLeaderId()).orElseThrow(() -> new RuntimeException());
        newLeader.setDepartmentId(currentTeamData.getDepartmentId());
        newLeader.setTeamId(teamDTO.getId());
        newLeader.setDutiesId(3);
        userRepository.save(newLeader);

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

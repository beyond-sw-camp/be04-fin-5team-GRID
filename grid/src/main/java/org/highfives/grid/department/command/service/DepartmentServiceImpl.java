package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.highfives.grid.department.command.aggregate.Department;
import org.highfives.grid.department.command.exception.DepartmentNotFoundException;
import org.highfives.grid.department.command.repository.DepartmentRepository;
import org.highfives.grid.department.command.repository.TeamRepository;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
import org.highfives.grid.user.command.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "commandDepartmentService")
public class DepartmentServiceImpl implements DepartmentService {


    private final DepartmentRepository departmentRepository;

    private final TeamRepository teamRepository;

    private final UserService userService;

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, TeamRepository teamRepository, UserService userService, UserRepository userRepository, ModelMapper mapper) {
        this.departmentRepository = departmentRepository;
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }




    @Override
    public DepartmentDTO findDepartmentById(int id) {
        // 소스 객체와 대상 객체 간에 필드를 매핑할 때 사용되는 매핑 전략을 정의
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Department findDepartmentInfo = null;

        try {
            findDepartmentInfo = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

            return mapper.map(findDepartmentInfo, DepartmentDTO.class);

        } catch (DepartmentNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public DepartmentDTO registDepartment(DepartmentDTO departmentDTO) {

        // 사용자가 이미 팀장인지 확인
        boolean isTeamLeader = teamRepository.existsByLeaderId(departmentDTO.getLeaderId());
        if (isTeamLeader) {
            throw new IllegalArgumentException("해당 사용자는 이미 팀장으로 등록되어 있습니다.");
        }


        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        // 새로운 부서 생성
        Department department = Department.builder()
                .departmentName(departmentDTO.getDepartmentName())
                .departmentStatus("Y")
                .departmentCode(departmentDTO.getDepartmentCode())
                .leaderId(departmentDTO.getLeaderId())
                .startTime(formattedDate)
                .sequence(departmentDTO.getSequence())
                .build();

        departmentRepository.save(department);

        // 부서장 정보 업데이트
        Employee leader = userRepository.findById(departmentDTO.getLeaderId()).orElseThrow(() -> new RuntimeException("리더를 찾을 수 없습니다."));
        leader.setDepartmentId(department.getId());
        leader.setDutiesId(2);
        userRepository.save(leader);

        return mapper.map(department, DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> modifyAllDepartment(List<DepartmentDTO> departmentDTOList) {

        List<DepartmentDTO> updateList = new ArrayList<>();

        for (DepartmentDTO modifyData : departmentDTOList) {

            DepartmentDTO currentDepartmentInfo = findDepartmentById(modifyData.getId());

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            String departmentStatus = modifyData.getDepartmentStatus() != null ? modifyData.getDepartmentStatus() : "Y";

            if (!"N".equals(departmentStatus)) {
                Department department = Department.builder()
                        .id(modifyData.getId())
                        .departmentName(modifyData.getDepartmentName())
                        .departmentStatus(departmentStatus)
                        .startTime(currentDepartmentInfo.getStartTime())
                        .endTime(currentDepartmentInfo.getEndTime())
                        .memberCnt(currentDepartmentInfo.getMemberCnt())
                        .leaderId(modifyData.getLeaderId())
                        .departmentCode(modifyData.getDepartmentCode())
                        .sequence(modifyData.getSequence())
                        .build();

                departmentRepository.save(department);
                updateList.add(mapper.map(department, DepartmentDTO.class));
            } else {
                Department department = Department.builder()
                        .id(modifyData.getId())
                        .departmentName(modifyData.getDepartmentName())
                        .departmentStatus(departmentStatus)
                        .startTime(currentDepartmentInfo.getStartTime())
                        .endTime(formattedDate)
                        .memberCnt(currentDepartmentInfo.getMemberCnt())
                        .leaderId(modifyData.getLeaderId())
                        .departmentCode(modifyData.getDepartmentCode())
                        .sequence(modifyData.getSequence())
                        .build();

                departmentRepository.save(department);
                updateList.add(mapper.map(department, DepartmentDTO.class));
            }
        }

        return updateList;
    }


    @Override
    public DepartmentDTO modifyDepartment(DepartmentDTO departmentDTO) {
        DepartmentDTO currentDepartmentInfo = findDepartmentById(departmentDTO.getId());

        if(!departmentDTO.getDepartmentStatus().equals("N")) {
            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .departmentName(departmentDTO.getDepartmentName())
                    .departmentStatus(departmentDTO.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(currentDepartmentInfo.getEndTime())
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .departmentCode(departmentDTO.getDepartmentCode())
                    .sequence(departmentDTO.getSequence())
                    .build();

            departmentRepository.save(department);
            return mapper.map(department, DepartmentDTO.class);
        }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .departmentName(departmentDTO.getDepartmentName())
                    .departmentStatus(departmentDTO.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(formattedDate)
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .sequence(departmentDTO.getSequence())
                    .departmentCode(departmentDTO.getDepartmentCode())
                    .build();

            departmentRepository.save(department);
            return mapper.map(department, DepartmentDTO.class);

    }

    @Override
    public void deleteDepartment(int id) {

        departmentRepository.deleteById(id);

    }

    @Override
    public List<DepartmentDTO> modifyDepartmentStatus(List<DepartmentDTO> departmentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<DepartmentDTO> departmentDTOList = departmentDTO.stream()
                .map(modifyData -> {
                    DepartmentDTO currentDepartmentInfo = findDepartmentById(modifyData.getId());

                    String newDepartmentStatus = "Y".equals(modifyData.getDepartmentStatus()) ? "N" : "Y";

                    String endTime = "Y".equals(newDepartmentStatus) ? null : dateFormat.format(new Date());

                    Department department = Department.builder()
                            .id(modifyData.getId())
                            .departmentName(modifyData.getDepartmentName())
                            .departmentStatus(newDepartmentStatus) // 변경된 상태 설정
                            .startTime(currentDepartmentInfo.getStartTime())
                            .endTime(endTime) // 변경된 endTime 설정
                            .memberCnt(currentDepartmentInfo.getMemberCnt())
                            .leaderId(modifyData.getLeaderId())
                            .sequence(modifyData.getSequence())
                            .departmentCode(modifyData.getDepartmentCode())
                            .build();

                    departmentRepository.save(department);
                    return mapper.map(department, DepartmentDTO.class);
                })
                .collect(Collectors.toList());

        return departmentDTOList;
    }

    @Override
    public DepartmentDTO modifyDepartmentLeader(DepartmentDTO departmentDTO) {

        // 사용자가 이미 팀장인지 확인
        boolean isTeamLeader = teamRepository.existsByLeaderId(departmentDTO.getLeaderId());
        if (isTeamLeader) {
            throw new IllegalArgumentException("해당 사용자는 이미 팀장으로 등록되어 있습니다.");
        }

        DepartmentDTO currentDepartmentInfo = findDepartmentById(departmentDTO.getId());

        Employee previousLeader = userRepository.findById(currentDepartmentInfo.getLeaderId()).orElseThrow(()-> new RuntimeException());
        if (previousLeader != null) {
            previousLeader.setDutiesId(4);
            userRepository.save(previousLeader);
        }

            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .departmentName(currentDepartmentInfo.getDepartmentName())
                    .departmentStatus(currentDepartmentInfo.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(currentDepartmentInfo.getEndTime())
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .departmentCode(currentDepartmentInfo.getDepartmentCode())
                    .sequence(currentDepartmentInfo.getSequence())
                    .build();



            departmentRepository.save(department);

            Employee leader = userRepository.findById(department.getLeaderId()).orElseThrow(() -> new RuntimeException());
            if (leader != null) {
            leader.setDepartmentId(departmentDTO.getId());
            leader.setDutiesId(2);
            leader.setTeamId(previousLeader.getTeamId());
                userRepository.save(leader);
            }


        return mapper.map(department, DepartmentDTO.class);
    }
}

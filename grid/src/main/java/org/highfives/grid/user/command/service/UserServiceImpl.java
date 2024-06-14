package org.highfives.grid.user.command.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.department.command.aggregate.Department;
import org.highfives.grid.department.command.aggregate.Team;
import org.highfives.grid.department.command.repository.DepartmentRepository;
import org.highfives.grid.department.command.repository.TeamRepository;
import org.highfives.grid.department.command.service.DepartmentServiceImpl;
import org.highfives.grid.department.command.service.TeamServiceImpl;
import org.highfives.grid.user.command.aggregate.*;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
import org.highfives.grid.user.exception.InvalidInfoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.highfives.grid.user.command.aggregate.YN.N;
import static org.highfives.grid.user.command.aggregate.YN.Y;

@Service("UserCommandService")
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TeamRepository teamRepository,
                           DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.teamRepository = teamRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public UserDTO addNewUser(UserDTO givenInfo,
                              Map<String, String> uploadedImg) {

        UserDTO submitInfo = inputSubData(givenInfo);
        submitInfo.setPwd(encodePwd(submitInfo.getPwd()));

        Employee addInfo = dtoToEntity(submitInfo);
        userRepository.save(addInfo);

        Employee addResult = userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber());

        return modelMapper.map(addResult, UserDTO.class);
    }

    @Override
    @Transactional
    public List<UserDTO> addMultiUser(List<UserDTO> givenInfo) {

        List<UserDTO> addResultList = new ArrayList<>();
        for (UserDTO userInfo : givenInfo) {
            try{
                UserDTO submitInfo = inputSubData(userInfo);

                submitInfo.setPwd(encodePwd(submitInfo.getPwd()));
                Employee employee = dtoToEntity(submitInfo);

                userRepository.save(employee);

                UserDTO addedUser = modelMapper.map(userRepository.findByEmail(userInfo.getEmail()), UserDTO.class);
                addResultList.add(addedUser);

            } catch(Exception e){
                log.error("Failed to add user: " + userInfo.getEmail(), e);
                throw e;
            }
        }

        return addResultList;
    }

    @Override
    @Transactional
    public UserDTO modifyUser(int id, UserDTO modifyInfo) {

        // 기존 정보 불러오기
        Employee oldInfo = userRepository.findById(id).orElseThrow(NullPointerException::new);

        // 기존 정보와 새 정보 비교해서 정보 넣기
        userRepository.save(inputNewInfo(oldInfo, modifyInfo));

        Employee resultInfo = userRepository.findById(modifyInfo.getId()).orElseThrow(NullPointerException::new);
        return modelMapper.map(resultInfo, UserDTO.class);
    }

    @Override
    @Transactional
    public List<UserDTO> modifyMultiUser(List<UserDTO> modifyList) {

        if(modifyList == null)
            throw new InvalidInfoException("Info list is empty");

        List<Employee> employeeList = new ArrayList<>();

        // 유저 검증 단계
        for (UserDTO userDTO : modifyList) {
            Employee oldInfo = userRepository.findByEmployeeNumber(userDTO.getEmployeeNumber());
            if (oldInfo == null || !oldInfo.getEmployeeName().equals(userDTO.getName())) {
                throw new UsernameNotFoundException("유저명과 사번이 일치하지 않습니다: " + userDTO.getEmail());
            }
            employeeList.add(inputNewMultiInfo(oldInfo, userDTO));
        }

        // 검증에 성공한 경우 수정 단계
        userRepository.saveAll(employeeList);

        List<UserDTO> resultList = new ArrayList<>();
        for (Employee employee : employeeList) {
            Employee result = userRepository.findById(employee.getId()).orElseThrow(NullPointerException::new);
            resultList.add(modelMapper.map(result, UserDTO.class));
        }

        return resultList;
    }

    @Override
    @Transactional
    public boolean deleteUser(String eNum) {

        if(eNum.isEmpty() || eNum == null)
            throw new InvalidInfoException("Employee number is empty");

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String resignTime = simpleDateFormat.format(new Date());

            Employee userInfo = userRepository.findByEmployeeNumber(eNum);
            userInfo.setResignYn(Y);
            userInfo.setResignTime(resignTime);
            userInfo.setAbsenceYn(Y);
            userInfo.setAbsenceContent("퇴사");

            userRepository.save(userInfo);
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean rejoinUser(String eNum) {

        try {
            Employee userInfo = userRepository.findByEmployeeNumber(eNum);
            userInfo.setResignYn(N);
            userInfo.setResignTime(null);
            userInfo.setAbsenceYn(N);
            userInfo.setAbsenceContent(null);

            userRepository.save(userInfo);
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    // 중복 값 입력 예외 처리 메소드
    @Override
    public String duplicateInfoCheck(UserDTO givenInfo) {
        try {
            String emailCheckResult = checkDuplicateEmail(givenInfo.getEmail());
            if (!emailCheckResult.equals("Pass")) {
                return emailCheckResult;
            }

            String employeeNumberCheckResult = checkDuplicateEmployeeNumber(givenInfo.getEmployeeNumber());
            if (!employeeNumberCheckResult.equals("Pass")) {
                return employeeNumberCheckResult;
            }

            String phoneNumberCheckResult = checkDuplicatePhoneNumber(givenInfo.getPhoneNumber());
            if (!phoneNumberCheckResult.equals("Pass")) {
                return phoneNumberCheckResult;
            }
        } catch (Exception e) {
            return "Unexpected error occurred: " + e.getMessage();
        }

        return "Pass";
    }

    private String checkDuplicateEmail(String email) {
        try {
            Employee employee = userRepository.findByEmail(email);
            if (employee != null) {
                if (employee.getEmail().equals(email)){
                    return "Pass";
                }
                return "Already used Email: " + email;
            }
        } catch (Exception e) {
            return "Error while checking Email: " + e.getMessage();
        }
        return "Pass";
    }

    private String checkDuplicateEmployeeNumber(String employeeNumber) {
        try {
            Employee employee = userRepository.findByEmployeeNumber(employeeNumber);
            if (employee != null) {
                if (employee.getEmployeeNumber().equals(employeeNumber)){
                    return "Pass";
                }
                return "Already used Employee Number: " + employeeNumber;
            }
        } catch (Exception e) {
            return "Error while checking Employee Number: " + e.getMessage();
        }
        return "Pass";
    }

    private String checkDuplicatePhoneNumber(String phoneNumber) {
        try {
            Employee employee = userRepository.findByPhoneNumber(phoneNumber);
            if (employee != null) {
                if (employee.getPhoneNumber().equals(phoneNumber)){
                    return "Pass";
                }
                return "Already used Phone Number: " + phoneNumber;
            }
        } catch (Exception e) {
            return "Error while checking Phone Number: " + e.getMessage();
        }
        return "Pass";
    }

    @Override
    public String multiInfoInputCheck(List<UserDTO> modifyList) {

        Set<String> emailCheck = new HashSet<>();
        Set<String> eNumCheck = new HashSet<>();
        Set<String> phoneNumCheck = new HashSet<>();

        for (UserDTO userDTO : modifyList) {
            emailCheck.add(userDTO.getEmail());
            eNumCheck.add(userDTO.getEmployeeNumber());
            phoneNumCheck.add(userDTO.getPhoneNumber());
        }

        if (emailCheck.size() != modifyList.size() ||
                eNumCheck.size() != modifyList.size() ||
                phoneNumCheck.size() != modifyList.size()) {
            return "NP";
        }

        return "P";
    }

    @Override
    @Transactional
    public boolean resetPwd(Map<String, String> infos) {

        if(infos.isEmpty())
            throw new InvalidInfoException("Change info is empty");

        try{
            Employee employee = userRepository.findByEmail(infos.get("email"));
            employee.setPwd(encodePwd(infos.get("pwd")));
            userRepository.save(employee);
            return true;
        } catch (Exception e) {
            log.info("Exception occurred: {}", e.getMessage());
            return false;
        }
    }

    public boolean changeGender(int userId) {

        Employee employee = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        if(employee.getGender() == Gender.F ) {
            employee.setGender(Gender.P);
            userRepository.save(employee);
            return true;
        }
        if(employee.getGender() == Gender.P) {
            employee.setGender(Gender.F);
            userRepository.save(employee);
            return true;
        }
        return false;
    }

    private Employee dtoToEntity(UserDTO givenInfo) {

        return Employee.builder()
                .email(givenInfo.getEmail())
                .pwd(givenInfo.getPwd())
                .employeeName(givenInfo.getName())
                .employeeNumber(givenInfo.getEmployeeNumber())
                .gender(givenInfo.getGender())
                .phoneNumber(givenInfo.getPhoneNumber())
                .joinTime(givenInfo.getJoinTime())
                .joinType(givenInfo.getJoinType())
                .workType(givenInfo.getWorkType())
                .contractStartTime(givenInfo.getContractStartTime())
                .contractEndTime(givenInfo.getContractEndTime())
                .role(Role.ROLE_USER)
                .dutiesId(givenInfo.getDutiesId())
                .positionId(givenInfo.getPositionId())
                .departmentId(givenInfo.getDepartmentId())
                .teamId(givenInfo.getTeamId())
                .resignYn(givenInfo.getResignYn())
                .absenceYn(givenInfo.getAbsenceYn())
                .build();
    }

    private String encodePwd(String pwd) {

        return bCryptPasswordEncoder.encode(pwd);
    }

    public boolean idCheck(int id, UserDTO givenInfo) {

        return id == givenInfo.getId();
    }

    private Employee inputNewInfo(Employee oldInfo, UserDTO givenInfo) {

        if(givenInfo.getEmail() == null)
            givenInfo.setEmail(oldInfo.getEmail());
        if(givenInfo.getName() == null)
            givenInfo.setName(oldInfo.getEmployeeName());
        if(givenInfo.getEmployeeNumber() == null)
            givenInfo.setEmployeeNumber(oldInfo.getEmployeeNumber());
        if(givenInfo.getPhoneNumber() == null)
            givenInfo.setPhoneNumber(oldInfo.getPhoneNumber());
        if(givenInfo.getCallNumber() == null)
            givenInfo.setCallNumber(oldInfo.getCallNumber());
        if(givenInfo.getDepartmentId() == 0)
            givenInfo.setDepartmentId(oldInfo.getDepartmentId());
        if(givenInfo.getTeamId() == 0)
            givenInfo.setTeamId(oldInfo.getTeamId());
        if(givenInfo.getPositionId() == 0)
            givenInfo.setPositionId(oldInfo.getPositionId());
        if(givenInfo.getDutiesId() == 0) {
            givenInfo.setDutiesId(oldInfo.getDutiesId());
        }
        if (givenInfo.getDutiesId() == 3) { // 팀장으로 변경할 경우 해당 팀 불러와서 leaderid 변경
            Team team = teamRepository.findById(givenInfo.getTeamId()).orElseThrow(NullPointerException::new);
            Employee oldLeader = userRepository.findById(team.getLeaderId()).orElseThrow(NullPointerException::new);
            team.setLeaderId(oldInfo.getId());
            oldLeader.setDutiesId(4);
            teamRepository.save(team);
            userRepository.save(oldLeader);
        }
        if (givenInfo.getDutiesId() == 2) { // 부서장으로 변경할 경우 해당 팀 불러와서 leaderid 변경
            Department department = departmentRepository.findById(givenInfo.getDepartmentId()).orElseThrow(NullPointerException::new);
            Employee oldLeader = userRepository.findById(department.getLeaderId()).orElseThrow(NullPointerException::new);
            department.setLeaderId(oldInfo.getId());
            oldLeader.setDutiesId(4);
            departmentRepository.save(department);
            userRepository.save(oldLeader);
        }
        if(givenInfo.getWorkType() == null)
            givenInfo.setWorkType(oldInfo.getWorkType());
        if(givenInfo.getContractEndTime() == null)
            givenInfo.setContractEndTime(oldInfo.getContractEndTime());
        if(givenInfo.getZipCode() == null)
            givenInfo.setZipCode(oldInfo.getZipCode());
        if(givenInfo.getAddress() == null)
            givenInfo.setAddress(oldInfo.getAddress());

        //관리자일 경우 1 ADMIN , 1이외의 유저일 경우 USER 넣음
        if(oldInfo.getId() == 1)
            givenInfo.setRole(Role.ROLE_ADMIN);
        if(oldInfo.getId() != 1)
            givenInfo.setRole(Role.ROLE_USER);

        return Employee.builder()
                .id(oldInfo.getId())
                .email(givenInfo.getEmail())
                .pwd(oldInfo.getPwd())
                .employeeName(givenInfo.getName())
                .employeeNumber(givenInfo.getEmployeeNumber())
                .gender(oldInfo.getGender())
                .phoneNumber(givenInfo.getPhoneNumber())
                .callNumber(givenInfo.getCallNumber())
                .zipCode(givenInfo.getZipCode())
                .address(givenInfo.getAddress())
                .assignedTask(givenInfo.getAssignedTask())
                .joinTime(givenInfo.getJoinTime())
                .joinType(givenInfo.getJoinType())
                .resignTime(oldInfo.getResignTime())
                .resignYn(oldInfo.getResignYn())
                .workType(givenInfo.getWorkType())
                .role(givenInfo.getRole())
                .contractStartTime(oldInfo.getContractStartTime())
                .contractEndTime(oldInfo.getContractEndTime())
                .salary(oldInfo.getSalary())
                .absenceYn(oldInfo.getAbsenceYn())
                .absenceContent(oldInfo.getAbsenceContent())
                .dutiesId(givenInfo.getDutiesId())
                .positionId(givenInfo.getPositionId())
                .teamId(givenInfo.getTeamId())
                .departmentId(givenInfo.getDepartmentId())
                .build();
    }

    private Employee inputNewMultiInfo(Employee oldInfo, UserDTO givenInfo) {

        // 수정 받는 정보들 중에 null 값이 있을 시 기존 값으로 입력받음.
        if(givenInfo.getDepartmentId() == 0)
            givenInfo.setDepartmentId(oldInfo.getDepartmentId());
        if(givenInfo.getTeamId() == 0)
            givenInfo.setTeamId(oldInfo.getTeamId());
        if(givenInfo.getPositionId() == 0)
            givenInfo.setPositionId(oldInfo.getPositionId());
        if(givenInfo.getDutiesId() == 0)
            givenInfo.setDutiesId(oldInfo.getDutiesId());
        if (givenInfo.getDutiesId() == 3) { // 팀장으로 변경할 경우 해당 팀 불러와서 leaderid 변경
            Team team = teamRepository.findById(givenInfo.getTeamId()).orElseThrow(NullPointerException::new);
            Employee oldLeader = userRepository.findById(team.getLeaderId()).orElseThrow(NullPointerException::new);
            team.setLeaderId(oldInfo.getId());
            oldLeader.setDutiesId(4);
            teamRepository.save(team);
            userRepository.save(oldLeader);
        }
        if (givenInfo.getDutiesId() == 2) { // 부서장으로 변경할 경우 해당 팀 불러와서 leaderid 변경
            Department department = departmentRepository.findById(givenInfo.getDepartmentId()).orElseThrow(NullPointerException::new);
            Employee oldLeader = userRepository.findById(department.getLeaderId()).orElseThrow(NullPointerException::new);
            department.setLeaderId(oldInfo.getId());
            oldLeader.setDutiesId(4);
            departmentRepository.save(department);
            userRepository.save(oldLeader);
        }
        if(givenInfo.getWorkType() == null)
            givenInfo.setWorkType(oldInfo.getWorkType());
        if(givenInfo.getContractEndTime() == null)
            givenInfo.setContractEndTime(oldInfo.getContractEndTime());
        if(givenInfo.getZipCode() == null)
            givenInfo.setZipCode(oldInfo.getZipCode());
        if(givenInfo.getAddress() == null)
            givenInfo.setAddress(oldInfo.getAddress());

        return Employee.builder()
                .id(oldInfo.getId())
                .email(oldInfo.getEmail())
                .pwd(oldInfo.getPwd())
                .employeeName(oldInfo.getEmployeeName())
                .employeeNumber(oldInfo.getEmployeeNumber())
                .gender(oldInfo.getGender())
                .phoneNumber(oldInfo.getPhoneNumber())
                .callNumber(oldInfo.getCallNumber())
                .zipCode(givenInfo.getZipCode())
                .address(givenInfo.getAddress())
                .assignedTask(oldInfo.getAssignedTask())
                .joinTime(oldInfo.getJoinTime())
                .joinType(oldInfo.getJoinType())
                .resignTime(oldInfo.getResignTime())
                .resignYn(oldInfo.getResignYn())
                .workType(givenInfo.getWorkType())
                .role(Role.ROLE_USER)
                .contractStartTime(oldInfo.getContractStartTime())
                .contractEndTime(givenInfo.getContractEndTime())
                .salary(oldInfo.getSalary())
                .absenceYn(oldInfo.getAbsenceYn())
                .absenceContent(oldInfo.getAbsenceContent())
                .dutiesId(givenInfo.getDutiesId())
                .positionId(givenInfo.getPositionId())
                .teamId(givenInfo.getTeamId())
                .departmentId(givenInfo.getDepartmentId())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee tokenInfo = userRepository.findByEmail(email);

        if (tokenInfo.getResignYn() == Y) {
            throw new RuntimeException("Resigned User");
        }

        return new PrincipalDetails(tokenInfo, true, true);
    }

    private UserDTO inputSubData(UserDTO givenInfo) {

        if(givenInfo.getContractStartTime() == null)
            givenInfo.setContractStartTime(givenInfo.getJoinTime());
        if(givenInfo.getContractEndTime() == null)
            givenInfo.setContractEndTime("-");

        givenInfo.setAbsenceYn(YN.N);
        givenInfo.setResignYn(YN.N);

        return givenInfo;
    }
}

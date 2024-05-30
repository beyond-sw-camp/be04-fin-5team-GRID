package org.highfives.grid.user.command.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.user.command.aggregate.*;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
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

import static org.highfives.grid.user.command.aggregate.YN.Y;

@Service("UserCommandService")
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public UserDTO addNewUser(UserDTO givenInfo,
                              Map<String, String> uploadedImg) {

        UserDTO submitInfo = inputSubData(givenInfo);
        submitInfo.setPwd(encodePwd(submitInfo.getPwd()));

        Employee addInfo = dTOtoEntity(submitInfo);
        userRepository.save(addInfo);

        Employee addResult = userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber());

        return modelMapper.map(addResult, UserDTO.class);
    }

    @Override
    @Transactional
    public List<UserDTO> addMultiUser(List<UserDTO> givenInfo) {

        List<UserDTO> addResultList = new ArrayList<>();
        for (UserDTO userInfo : givenInfo) {

            UserDTO submitInfo = inputSubData(userInfo);

            submitInfo.setPwd(encodePwd(submitInfo.getPwd()));
            Employee employee = dTOtoEntity(submitInfo);

            userRepository.save(employee);

            addResultList
                    .add(modelMapper
                            .map(userRepository.findByEmail(userInfo.getEmail()), UserDTO.class));
        }

        return addResultList;
    }

    @Override
    @Transactional
    public UserDTO modifyUser(int id, UserDTO modifyInfo) {

        Employee oldInfo = userRepository.findById(id).orElseThrow(NullPointerException::new);
        userRepository.save(inputNewInfo(oldInfo, modifyInfo));
        Employee resultInfo = userRepository.findById(modifyInfo.getId()).orElseThrow(NullPointerException::new);
        return modelMapper.map(resultInfo, UserDTO.class);
    }

    @Override
    @Transactional
    public List<UserDTO> modifyMultiUser(List<UserDTO> modifyList) {
        List<Employee> employeeList = new ArrayList<>();

        // 유저 검증 단계
        for (UserDTO userDTO : modifyList) {
            Employee oldInfo = userRepository.findByEmployeeNumber(userDTO.getEmployeeNumber());
            if (oldInfo == null || !oldInfo.getEmployeeName().equals(userDTO.getName())) {
                throw new UsernameNotFoundException("유저명과 사번이 일치하지 않습니다: " + userDTO.getEmail());
            }
            employeeList.add(inputNewMulitInfo(oldInfo, userDTO));
        }
        System.out.println("employeeList = " + employeeList);
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

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String resignTime = simpleDateFormat.format(new Date());

            Employee userInfo = userRepository.findByEmployeeNumber(eNum);
            userInfo.setResignYn(Y);
            userInfo.setResignTime(resignTime);

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

            Employee employee = userRepository.findByEmail(givenInfo.getEmail());
            if (employee != null) {
                if(employee.getEmail().equals(givenInfo.getEmail())) {
                    return "Pass";
                }
                return "Already used Email : " + givenInfo.getEmail();
            }

            employee = userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber());
            if (employee != null) {
                if(employee.getEmployeeName().equals(givenInfo.getEmployeeNumber())) {
                    return "Pass";
                }
                return "Already used Employee Number : " + givenInfo.getEmployeeNumber();
            }

            employee = userRepository.findByPhoneNumber(givenInfo.getPhoneNumber());
            if ( employee != null) {
                if(employee.getPhoneNumber().equals(givenInfo.getPhoneNumber())) {
                    return "Pass";
                }
                return "Already used Phone Number : " + givenInfo.getPhoneNumber();
            }
        } catch (Exception e) {
            return "NP";
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

    private Employee dTOtoEntity(UserDTO givenInfo) {

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
                .role(Role.ROLE_USER)
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

    private Employee inputNewMulitInfo(Employee oldInfo, UserDTO givenInfo) {

        // 수정 받는 정보들 중에 null 값이 있을 시 기존 값으로 입력받음.
        if(givenInfo.getDepartmentId() == 0)
            givenInfo.setDepartmentId(oldInfo.getDepartmentId());
        if(givenInfo.getTeamId() == 0)
            givenInfo.setTeamId(oldInfo.getTeamId());
        if(givenInfo.getPositionId() == 0)
            givenInfo.setPositionId(oldInfo.getPositionId());
        if(givenInfo.getDutiesId() == 0)
            givenInfo.setDutiesId(oldInfo.getDutiesId());
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

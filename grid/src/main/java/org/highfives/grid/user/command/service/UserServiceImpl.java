package org.highfives.grid.user.command.service;

import org.highfives.grid.review.command.exception.NotFoundException;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.aggregate.YN;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service("UserCommandService")
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
    public UserDTO addNewUser(UserDTO givenInfo) {

        givenInfo.setPwd(encodePwd(givenInfo));

        Employee addInfo = dTOtoEntity(givenInfo);
        userRepository.save(addInfo);

        Employee addResult = userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber());

        return modelMapper.map(addResult, UserDTO.class);
    }

    @Override
    @Transactional
    public List<UserDTO> addMultiUser(List<UserDTO> givenInfo) {

        List<UserDTO> addResultList = new ArrayList<>();
        for (UserDTO userInfo : givenInfo) {
            userInfo.setPwd(encodePwd(userInfo));
            Employee employee = dTOtoEntity(userInfo);

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

        for (UserDTO userDTO : modifyList) {
            Employee oldInfo = userRepository.findById(userDTO.getId()).orElseThrow(NullPointerException::new);
            employeeList.add(inputNewInfo(oldInfo, userDTO));
        }

        userRepository.saveAll(employeeList);

        List<UserDTO> resultList = new ArrayList<>();

        for (int i = 0; i < employeeList.size(); i++) {
            Employee result =
                    userRepository.findById(modifyList.get(i).getId()).orElseThrow(NullPointerException::new);
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
            userInfo.setResignYn(YN.Y);
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
            if (userRepository.findByEmail(givenInfo.getEmail()) != null) {
                return "Already used Email : " + givenInfo.getEmail();
            }
            if (userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber()) != null) {
                return "Already used Employee Number : " + givenInfo.getEmployeeNumber();
            }
            if (userRepository.findByPhoneNumber(givenInfo.getPhoneNumber()) != null) {
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

        for (int i = 0; i < modifyList.size(); i++) {
            emailCheck.add(modifyList.get(i).getEmail());
            eNumCheck.add(modifyList.get(i).getEmployeeNumber());
            phoneNumCheck.add(modifyList.get(i).getPhoneNumber());
        }

        if (emailCheck.size() != modifyList.size() ||
                eNumCheck.size() != modifyList.size() ||
                phoneNumCheck.size() != modifyList.size()) {
            return "NP";
        }

        return "P";
    }

    private Employee dTOtoEntity(UserDTO givenInfo) {

        return new Employee (
                givenInfo.getEmail(),
                givenInfo.getPwd(),
                givenInfo.getName(),
                givenInfo.getEmployeeNumber(),
                givenInfo.getGender(),
                givenInfo.getPhoneNumber(),
                givenInfo.getJoinTime(),
                givenInfo.getJoinType(),
                givenInfo.getWorkType(),
                givenInfo.getContractStartTime(),
                givenInfo.getDutiesId(),
                givenInfo.getPositionId(),
                givenInfo.getDepartmentId(),
                givenInfo.getTeamId()
        );
    }

    private String encodePwd(UserDTO givenInfo) {

        return bCryptPasswordEncoder.encode(givenInfo.getPwd());
    }

    public boolean idCheck(int id, UserDTO givenInfo) {

        return id == givenInfo.getId();
    }

    private Employee inputNewInfo(Employee oldInfo, UserDTO givenInfo) {

        return Employee.builder()
                .id(oldInfo.getId())
                .email(givenInfo.getEmail())
                .pwd(encodePwd(givenInfo))
                .employeeName(givenInfo.getName())
                .employeeNumber(oldInfo.getEmployeeNumber())
                .gender(givenInfo.getGender())
                .phoneNumber(givenInfo.getPhoneNumber())
                .callNumber(givenInfo.getCallNumber())
                .zipCode(givenInfo.getZipCode())
                .address(givenInfo.getAddress())
                .assignedTask(givenInfo.getAssignedTask())
                .joinTime(givenInfo.getJoinTime())
                .joinType(givenInfo.getJoinType())
                .resignTime(givenInfo.getResignTime())
                .resignYn(givenInfo.getResignYn())
                .workType(givenInfo.getWorkType())
                .contractStartTime(givenInfo.getContractStartTime())
                .contractEndTime(givenInfo.getContractEndTime())
                .salary(givenInfo.getSalary())
                .absenceYn(givenInfo.getAbsenceYn())
                .absenceContent(givenInfo.getAbsenceContent())
                .dutiesId(givenInfo.getDutiesId())
                .positionId(givenInfo.getPositionId())
                .teamId(givenInfo.getTeamId())
                .departmentId(givenInfo.getDepartmentId())
                .build();
    }
}

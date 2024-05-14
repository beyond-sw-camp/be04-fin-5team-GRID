package org.highfives.grid.user.command.service;

import jakarta.transaction.Transactional;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public UserDTO modifyUser(int id, UserDTO modifyInfo) {

        Employee oldInfo = userRepository.findById(id).orElseThrow(NullPointerException::new);

        oldInfo = inputNewInfo(oldInfo, modifyInfo);

        userRepository.save(oldInfo);

        Employee resultInfo = userRepository.findById(modifyInfo.getId()).orElseThrow(NullPointerException::new);

        return modelMapper.map(resultInfo, UserDTO.class);
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

        oldInfo.setEmail(givenInfo.getEmail());
        oldInfo.setPwd(encodePwd(givenInfo));
        oldInfo.setEmployeeName(givenInfo.getName());
        oldInfo.setGender(givenInfo.getGender());
        oldInfo.setPhoneNumber(givenInfo.getPhoneNumber());
        oldInfo.setCallNumber(givenInfo.getCallNumber());
        oldInfo.setZipCode(givenInfo.getZipCode());
        oldInfo.setAddress(givenInfo.getAddress());
        oldInfo.setJoinTime(givenInfo.getJoinTime());
        oldInfo.setJoinType(givenInfo.getJoinType());
        oldInfo.setResignTime(givenInfo.getResignTime());
        oldInfo.setResignYn(givenInfo.getResignYn());
        oldInfo.setWorkType(givenInfo.getWorkType());
        oldInfo.setContractStartTime(givenInfo.getContractStartTime());
        oldInfo.setContractEndTime(givenInfo.getContractEndTime());
        oldInfo.setSalary(givenInfo.getSalary());
        oldInfo.setAbsenceYn(givenInfo.getAbsenceYn());
        oldInfo.setAbsenceContent(givenInfo.getAbsenceContent());
        oldInfo.setDutiesId(givenInfo.getDutiesId());
        oldInfo.setPositionId(givenInfo.getPositionId());
        oldInfo.setTeamId(givenInfo.getTeamId());
        oldInfo.setDepartmentId(givenInfo.getDepartmentId());

        return oldInfo;
    }
}

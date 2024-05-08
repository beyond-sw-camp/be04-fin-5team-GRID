package org.highfives.grid.user.command.service;

import jakarta.transaction.Transactional;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
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

        encodePwd(givenInfo);

        Employee addInfo = dTOtoEntity(givenInfo);
        userRepository.save(addInfo);

        Employee addResult = userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber());

        return modelMapper.map(addResult, UserDTO.class);
    }

    // 회원 가입 시 예외 처리 메소드 ( 중복된 값 )
    @Override
    public String duplicateInfoCheck(UserDTO givenInfo) {

        try {
            if (userRepository.findByEmail(givenInfo.getEmail()) != null) {
                return "Duplicated Email";
            }
            if (userRepository.findByEmployeeNumber(givenInfo.getEmployeeNumber()) != null) {
                return "Duplicated Employee Number";
            }
            if (userRepository.findByPhoneNumber(givenInfo.getPhoneNumber()) != null) {
                return "Duplicated Phone Number";
            }
        } catch (Exception e) {
            return "NP";
        }

        return "Pass";
    }

    private Employee dTOtoEntity(UserDTO givenInfo) {

        Employee result = new Employee (
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

        return result;
    }

    private UserDTO encodePwd(UserDTO givenInfo) {

        givenInfo.setPwd(bCryptPasswordEncoder.encode(givenInfo.getPwd()));

        return givenInfo;
    }
}

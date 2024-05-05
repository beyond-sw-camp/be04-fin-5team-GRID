package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addNewUser(UserDTO givenInfo) {
        Employee addInfo = dTOtoEntity(givenInfo);

        userRepository.save(addInfo);


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

}

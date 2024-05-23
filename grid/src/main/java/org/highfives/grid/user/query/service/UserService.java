package org.highfives.grid.user.query.service;

import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUsers();

    LeaderInfoDTO findLeaderInfo(int id);

    UserDTO findUserByEmployeeNum(int eNum);

    UserDTO findUserById(int id);
}

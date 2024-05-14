package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO addNewUser(UserDTO givenInfo);

    String duplicateInfoCheck(UserDTO givenInfo);

    List<UserDTO> addMultiUser(List<UserDTO> givenInfo);

    UserDTO modifyUser(int id, UserDTO modifyInfo);

    boolean idCheck(int id, UserDTO modifyInfo);
}

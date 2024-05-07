package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.UserDTO;

public interface UserService {
    UserDTO addNewUser(UserDTO givenInfo);

    String duplicateInfoCheck(UserDTO givenInfo);
}

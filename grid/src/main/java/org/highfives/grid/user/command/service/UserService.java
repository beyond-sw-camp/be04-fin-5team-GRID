package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.UserDTO;

public interface UserService {
    void addNewUser(UserDTO givenInfo);
}

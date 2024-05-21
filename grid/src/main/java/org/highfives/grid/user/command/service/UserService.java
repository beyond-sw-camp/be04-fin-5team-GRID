package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO addNewUser(UserDTO givenInfo);

    String duplicateInfoCheck(UserDTO givenInfo);

    List<UserDTO> addMultiUser(List<UserDTO> givenInfo);

    UserDTO modifyUser(int id, UserDTO modifyInfo);

    boolean idCheck(int id, UserDTO modifyInfo);

    List<UserDTO> modifyMultiUser(List<UserDTO> modifyList);

    String multiInfoInputCheck(List<UserDTO> modifyList);

    boolean deleteUser(String employeeNumber);

    void findPwd(String pwd);

    boolean changeGender(int userId);
}

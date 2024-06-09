package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.vo.ReqResetPwdVO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    UserDTO addNewUser(UserDTO givenInfo, Map<String, String> uploadedImg);

    String duplicateInfoCheck(UserDTO givenInfo);

    List<UserDTO> addMultiUser(List<UserDTO> givenInfo);

    UserDTO modifyUser(int id, UserDTO modifyInfo);

    boolean idCheck(int id, UserDTO modifyInfo);

    List<UserDTO> modifyMultiUser(List<UserDTO> modifyList);

    String multiInfoInputCheck(List<UserDTO> modifyList);

    boolean deleteUser(String employeeNumber);

    boolean rejoinUser(String employeeNumber);

    boolean resetPwd(Map<String, String> infos);

    boolean changeGender(int userId);

}

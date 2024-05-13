package org.highfives.grid.user.command.controller;

import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.service.UserService;
import org.highfives.grid.user.command.vo.ResAddUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("UserCommandController")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 신규 유저 등록 (단일)
    @PostMapping
    public ResponseEntity<ResAddUserVO> addNewUser(@RequestBody UserDTO givenInfo) {

        if(duplicateInfoCheck(givenInfo) != null)
            return duplicateInfoCheck(givenInfo);

        givenInfo.setContractStartTime(givenInfo.getJoinTime());

        List<UserDTO> result = new ArrayList<>();
        result.add(userService.addNewUser(givenInfo));

        ResAddUserVO response = new ResAddUserVO(
            201, "Success to add new user", "/user", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 다중 유저 가입 ( csv 파싱은 프론트에서 처리해서 json 형태로 전해준다고 가정 )
    @PostMapping("/list")
    public ResponseEntity<ResAddUserVO> addMultiUser(@RequestBody List<UserDTO> infoList) {

        List<UserDTO> givenInfo = new ArrayList<>();

        for (UserDTO info : infoList) {
            if( duplicateInfoCheck(info) != null )
                return duplicateInfoCheck(info);
            info.setContractStartTime(info.getJoinTime());
            givenInfo.add(info);
        }

        List<UserDTO> result = userService.addMultiUser(givenInfo);
        ResAddUserVO response = new ResAddUserVO(
                201, "Success to add new users", "/user", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<ResAddUserVO> duplicateInfoCheck(UserDTO givenInfo) {

        if (!userService.duplicateInfoCheck(givenInfo).equals("Pass")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResAddUserVO(400,
                            userService.duplicateInfoCheck(givenInfo),
                            "/user", null ));
        }

        return null;
    }

}


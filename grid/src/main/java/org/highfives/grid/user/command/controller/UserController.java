package org.highfives.grid.user.command.controller;

import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.service.UserService;
import org.highfives.grid.user.command.vo.ResUserListVO;
import org.highfives.grid.user.command.vo.ResUserVO;
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
    public ResponseEntity<ResUserVO> addNewUser(@RequestBody UserDTO givenInfo) {

        if(duplicateInfoCheck(givenInfo) != null)
            return duplicateInfoCheck(givenInfo);

        givenInfo.setContractStartTime(givenInfo.getJoinTime());

        UserDTO result = userService.addNewUser(givenInfo);

        ResUserVO response = new ResUserVO(
            201, "Success to add new user", "/user", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 다중 유저 가입 ( csv 파싱은 프론트에서 처리해서 json 형태로 전해준다고 가정 )
    @PostMapping("/list")
    public ResponseEntity<ResUserListVO> addMultiUser(@RequestBody List<UserDTO> infoList) {

        List<UserDTO> givenInfo = new ArrayList<>();

        for (UserDTO info : infoList) {
            if( duplicateInfoCheck(info) != null ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResUserListVO (
                        400, duplicateInfoCheck(info).getBody().getMessage(),"/users", null));
            }
            info.setContractStartTime(info.getJoinTime());
            givenInfo.add(info);
        }

        List<UserDTO> result = userService.addMultiUser(givenInfo);
        ResUserListVO response = new ResUserListVO(
                201, "Success to add new users", "/users", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원 정보 단일 수정
    @PutMapping
    public ResponseEntity<ResUserVO> modifyUserInfo(@RequestBody UserDTO modifyInfo) {

        if(duplicateInfoCheck(modifyInfo) != null)
            return duplicateInfoCheck(modifyInfo);

        UserDTO result = userService.modifyUser(modifyInfo);
        ResUserVO response =
                new ResUserVO(200, "Success to modify info", "/users/{id}", result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/duplication")
    public ResponseEntity<ResUserVO> duplicateInfoCheck(UserDTO givenInfo) {

        if (!userService.duplicateInfoCheck(givenInfo).equals("Pass")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResUserVO(400,
                            userService.duplicateInfoCheck(givenInfo),
                            "/user", null ));
        }

        return null;
    }

}


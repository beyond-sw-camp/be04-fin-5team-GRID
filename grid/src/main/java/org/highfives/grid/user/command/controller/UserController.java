package org.highfives.grid.user.command.controller;

import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.service.UserService;
import org.highfives.grid.user.command.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 신규 유저 등록
    @PostMapping
    public ResponseEntity<ResponseVO> addNewUser(@RequestBody UserDTO givenInfo) {

        givenInfo = inputTimeData(givenInfo);

        userService.addNewUser(givenInfo);

        return null;
    }

    private UserDTO inputTimeData(UserDTO givenInfo) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = simpleDateFormat.format(LocalDate.now());

        givenInfo.setJoinTime(currentTime);
        givenInfo.setContractStartTime(currentTime);

        return givenInfo;
    }

}

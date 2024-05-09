package org.highfives.grid.user.query.controller;

import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.user.query.vo.ResFindListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserQueryController")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 전체 직원 조회
    @GetMapping()
    public ResponseEntity<ResFindListVO> findAllUsers() {

        userService.findAllUsers();



        return null;
    }

    // 사번으로 직원 조회

}

package org.highfives.grid.user.query.controller;

import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.user.query.vo.ResFindLeaderVO;
import org.highfives.grid.user.query.vo.ResFindListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 직원 id로 부서장/팀장 정보 조회
    @GetMapping("/{id}/leaders")
    public ResponseEntity<ResFindLeaderVO> findLeaderInfo(@PathVariable("id") int id) {

        LeaderInfoDTO info = userService.findLeaderInfo(id);

        ResFindLeaderVO response =
                new ResFindLeaderVO(200, "Success to find Leader info",
                        "/test", info);
        System.out.println("response = " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

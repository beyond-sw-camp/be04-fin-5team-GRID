package org.highfives.grid.user.query.controller;

import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.user.query.vo.ResFindLeaderVO;
import org.highfives.grid.user.query.vo.ResFindListVO;
import org.highfives.grid.user.query.vo.ResFindUserVO;
import org.highfives.grid.user.query.vo.SimpleInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("UserQueryController")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    // 전체 직원 조회
    @GetMapping("/list")
    public ResponseEntity<ResFindListVO> findAllUsers() {

        List<UserDTO> resultDTOs = userService.findAllUsers();
        List<SimpleInfo> resultList = DTOtoSimpleInfo(resultDTOs);

        System.out.println("resultDTOs = " + resultDTOs);
        ResFindListVO response =
                new ResFindListVO(200, "Success to find user list", "/users/{id}", resultList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 사번으로 직원 조회
    @GetMapping("/{employeeNumber}")
    public ResponseEntity<ResFindUserVO> findUserByEmployeeNum(@PathVariable("employeeNumber") int eNum) {

        UserDTO userDTO = userService.findUserByEmployeeNum(eNum);

        if(userDTO != null){
            ResFindUserVO response =
                new ResFindUserVO(200, "Success to find user", "/users/list", userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ResFindUserVO response =
                    new ResFindUserVO(404, "No matching user", "/users/{employeeNumber}", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 직원 id로 부서장/팀장 정보 조회
    @GetMapping("/{id}/leaders")
    public ResponseEntity<ResFindLeaderVO> findLeaderInfo(@PathVariable("id") int id) {

        LeaderInfoDTO info = userService.findLeaderInfo(id);

        ResFindLeaderVO response =
                new ResFindLeaderVO(200, "Success to find Leader info", "/test", info);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private List<SimpleInfo> DTOtoSimpleInfo(List<UserDTO> list) {

        List<SimpleInfo> result = new ArrayList<>();

        for (UserDTO user : list) {
            SimpleInfo info = modelMapper.map(user, SimpleInfo.class);
            result.add(info);
        }

        return result;
    }
}

package org.highfives.grid.user.query.controller;

import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.user.query.vo.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String name = null;
        List<UserDTO> resultDTOs = userService.findAllUsers();
        List<SimpleInfo> resultList = DTOtoSimpleInfo(resultDTOs);

        System.out.println("resultDTOs = " + resultDTOs);
        ResFindListVO response =
                new ResFindListVO(200, "Success to find user list", "/users/{id}", resultList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 이름에 해당하는 직원 조회
    @GetMapping("/list/{name}")
    public ResponseEntity<ResFindListVO> findUsersByName(@PathVariable("name") String name) {
        List<SimpleInfo> resultList = new ArrayList<>();

        if(name == null || name.trim().isEmpty() ){
            List<UserDTO> resultDTOs = userService.findAllUsers();
            resultList = DTOtoSimpleInfo(resultDTOs);
        }

        if(name != null) {
            List<UserDTO> resultDTOs = userService.findUsersByName(name);
            resultList = DTOtoSimpleInfo(resultDTOs);
        }

        ResFindListVO response =
                new ResFindListVO(200, "Success to find user list", "/users/{id}", resultList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 사번으로 직원 조회
    @GetMapping("/{employeeNumber}")
    public ResponseEntity<ResFindUserVO> findUserByEmployeeNum(@PathVariable("employeeNumber") String eNum) {

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

    //아이디로 사원 정보 조회
    @GetMapping("/id/{userID}")
    public ResponseEntity<ResFindUserVO> findUserById(@PathVariable("userID") int id) {

        UserDTO userDTO = userService.findUserById(id);

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

    //이메일로 사원 정보 조회
    @GetMapping("/mail/{email}")
    public ResponseEntity<ResFindUserVO> findUserByEmail(@PathVariable("email") String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

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

    // 이메일로 이름 체크
    @GetMapping("/{email}/name")
    public ResponseEntity<ResCheckNameVO> checkNameByEmail(@PathVariable("email") String email) {

        Map<String, Object> result = userService.checkNameByEmail(email);

        if(result != null){
            ResCheckNameVO response =
                    new ResCheckNameVO(200, "Success to find user", "/users/list", result);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ResCheckNameVO response =
                    new ResCheckNameVO(404, "No matching user", "/users/{employeeNumber}", null);
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


    // 팀 id로 해당 팀 직원들 조회
    @GetMapping("/team-list/{teamId}")
    public ResponseEntity<ResFindListVO> findTeamList(@PathVariable int teamId) {
         List<UserDTO> userDTOList = userService.findTeamList(teamId);

        List<SimpleInfo> simpleInfos = DTOtoSimpleInfo(userDTOList);

        ResFindListVO resFindListVO = ResFindListVO.builder()
                .message("success")
                .href("/")
                .statusCode(200)
                .result(simpleInfos)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(resFindListVO);
    }

    //직위 목록 조회
    @GetMapping("/positions")
    public ResponseEntity<ResPositionVO> findPositions() {

        List<PositionDTO> result = userService.findPositions();

        ResPositionVO response =
                new ResPositionVO(200, "Success to find positions", "/", result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    //직책 목록 조회
    @GetMapping("/duties")
    public ResponseEntity<ResDutiesVO> findDuties() {

        List<DutiesDTO> result = userService.findDuties();

        ResDutiesVO response =
                new ResDutiesVO(200, "Success to find positions", "/", result);

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

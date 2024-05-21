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

        System.out.println("givenInfo = " + givenInfo);

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

        //받아온 데이터 간 중복 체크
        if(!userService.multiInfoInputCheck(infoList).equals("P"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResUserListVO(
                            400, "Some infos are duplicated..!",
                            "/users/list", null));

        //계약일 자동 입력을 위한 List 객체 생성
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
    @PutMapping("/{id}")
    public ResponseEntity<ResUserVO> modifyUserInfo(@PathVariable("id") int id, @RequestBody UserDTO modifyInfo) {

        if(!userService.idCheck(id, modifyInfo))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResUserVO(400, "Unmatched Id info", "/users/{id}", null));

        if(duplicateInfoCheck(modifyInfo) != null)
            return duplicateInfoCheck(modifyInfo);

        UserDTO result = userService.modifyUser(id, modifyInfo);
        ResUserVO response =
                new ResUserVO(200, "Success to modify user info", "/users/{id}", result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원 정보 일괄 수정
    @PutMapping("/list")
    public ResponseEntity<ResUserListVO> modifyMultiUser(@RequestBody List<UserDTO> modifyList) {

        //받아온 데이터 간 중복 체크
        if(!userService.multiInfoInputCheck(modifyList).equals("P"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResUserListVO(
                            400, "Some infos are duplicated..!",
                            "/users/list", null));

        //받아온 데이터 간의 중복 체크가 정상적으로 종료 시, DB의 데이터와 중복 체크
        for(UserDTO info : modifyList) {
            if( duplicateInfoCheck(info) != null ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResUserListVO (
                                400, duplicateInfoCheck(info).getBody().getMessage(),
                                "/users", null));
            }
        }

        List<UserDTO> result = userService.modifyMultiUser(modifyList);
        ResUserListVO response =
                new ResUserListVO(200, "Success to modify all infos", "/users/list", result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{eNum}/status")
    public ResponseEntity<ResUserVO> resignUser(@PathVariable("eNum") String employeeNumber) {

        if(!userService.deleteUser(employeeNumber))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResUserVO(400, "No matched user", "/users/list", null)
            );

        ResUserVO response =
                new ResUserVO(200, "Success to resign " + employeeNumber + " user",
                        "/users/list", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/password")
    public ResponseEntity<ResUserVO> findPwd(@RequestBody String pwd) {

        userService.findPwd(pwd);

        return null;
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


package org.highfives.grid.user.command.controller;

import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.service.UserService;
import org.highfives.grid.user.command.vo.ResAddOneUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userCommand")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /*
* 프론트에서 받아야할 내용 :

1. email
2. pwd
3. name
4. employee_number
5. gender
6. phone_number
7. join_type
8. work_type
9. 직책 id
10. 직위 id
11. 팀 id
12. 부서 id
13. 입사일

회원 가입 시 엔터티로 넘겨야할 내용 :
1. email
2. pwd
3. name
4. employee_number
5. gender
6. phone_number
7. join_time
8. join_type
9. work_type
10. constract_start_time
11. 직책 id
12. 직위 id
13. 팀 id
14. 부서 id
*
*
* */
    // 유저 등록시 예외 사항? : 중복값 발생,
    // 신규 유저 등록 (단일)
    @PostMapping
    public ResponseEntity<ResAddOneUserVO> addNewUser(@RequestBody UserDTO givenInfo) {

        System.out.println("givenInfo = " + givenInfo);

        if(duplicateInfoCheck(givenInfo) != null)
            return duplicateInfoCheck(givenInfo);

        givenInfo.setContractStartTime(givenInfo.getJoinTime());

        System.out.println("givenInfo2 = " + givenInfo);
        UserDTO result = userService.addNewUser(givenInfo);

        ResAddOneUserVO response = new ResAddOneUserVO (
            201, "Success to add new user", "/user", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<ResAddOneUserVO> duplicateInfoCheck(UserDTO givenInfo) {

        if (!userService.duplicateInfoCheck(givenInfo).equals("OK")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResAddOneUserVO (400,
                            userService.duplicateInfoCheck(givenInfo),
                            "/user", null ));
        }

        return null;
    }
//    private UserDTO inputTimeData(UserDTO givenInfo) {
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String joinTime = simpleDateFormat.format(givenInfo.getJoinTime());
//
//        givenInfo.setJoinTime(joinTime);
//        givenInfo.setContractStartTime(joinTime);
//
//        return givenInfo;
//    }

}


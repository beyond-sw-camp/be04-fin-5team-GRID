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

@RestController("userCommand")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 신규 유저 등록
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

    @PostMapping
    public ResponseEntity<ResponseVO> addNewUser(@RequestBody UserDTO givenInfo) {

        givenInfo = inputTimeData(givenInfo);

        userService.addNewUser(givenInfo);

        return null;
    }

    private UserDTO inputTimeData(UserDTO givenInfo) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String joinTime = simpleDateFormat.format(givenInfo.getJoinTime());

        givenInfo.setJoinTime(joinTime);
        givenInfo.setContractStartTime(joinTime);

        return givenInfo;
    }

}


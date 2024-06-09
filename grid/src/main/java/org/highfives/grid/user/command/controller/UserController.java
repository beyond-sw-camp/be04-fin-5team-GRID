package org.highfives.grid.user.command.controller;

import org.highfives.grid.user.command.dto.UserDTO;
import org.highfives.grid.user.command.service.ImgService;
import org.highfives.grid.user.command.service.UserService;
import org.highfives.grid.user.command.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("UserCommandController")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ImgService imgService;

    @Autowired
    public UserController(UserService userService,
                          ImgService imgService) {
        this.userService = userService;
        this.imgService =imgService;
    }

    // 신규 유저 등록 (단일)
    @PostMapping
    public ResponseEntity<ResUserVO> addNewUser(@RequestBody UserDTO givenInfo,
                                                MultipartFile multipartFile) throws Exception{

        Map<String, String> uploadResult = new HashMap<>();

        if(multipartFile != null) {
            uploadResult = imgService.imgS3Upload(multipartFile);
        }

        if(duplicateInfoCheck(givenInfo) != null)
            return duplicateInfoCheck(givenInfo);

        UserDTO result = userService.addNewUser(givenInfo, uploadResult);

        ResUserVO response = new ResUserVO(
                201, "Success to add new user", "/user", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 다중 유저 가입 ( csv 파싱은 프론트에서 처리해서 json 형태로 전해준다고 가정 )12
    @PostMapping("/list")
    public ResponseEntity<ResUserListVO> addMultiUser(@RequestBody List<UserDTO> infoList) {

        System.out.println("infoList = " + infoList);

        if(!userService.multiInfoInputCheck(infoList).equals("P"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResUserListVO(
                            400, "Some given infos are duplicated..",
                            "/users/list", null));

        List<UserDTO> givenInfo = new ArrayList<>();

        for (UserDTO info : infoList) {
            if( duplicateInfoCheck(info) != null ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResUserListVO (
                                400, duplicateInfoCheck(info).getBody().getMessage(),"/users", null));
            }
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

    //이미지 업데이트(업로드)
    @PutMapping("/img")
    public ResponseEntity<ResImgUploadVO> uploadImage(@RequestPart("file") MultipartFile file,
                                                      @RequestParam("id") int id,
                                                      @RequestParam("typeId") int typeId) throws IOException {
        ResImgUploadVO response = new ResImgUploadVO();
        Map<String, String> result = new HashMap<>();

        try {
            result = imgService.imgS3Upload(file);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResImgUploadVO(400, "Failed to upload image",
                            "/users/profile/{id}", null));
        }

        String tableUpdateResult = imgService.imgTableUpdate(result, id, typeId);

        if (!tableUpdateResult.equals("Fail") && !tableUpdateResult.equals("No old img"))
            imgService.imgS3Delete(tableUpdateResult);

        response = new ResImgUploadVO(200,
                "Success to upload image", "/users/profile/{id}", result.get("path"));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원 정보 일괄 수정
    @PutMapping("/list")
    public ResponseEntity<ResUserListVO> modifyMultiUser(@RequestBody List<UserDTO> modifyList) {

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

    //퇴사/재입사
    @PutMapping("/{eNum}/status")
    public ResponseEntity<ResUserVO> modifyResignStatus(@PathVariable("eNum") String employeeNumber,
                                                        @RequestBody ReqResignStatusVO resign ) {

        //퇴사(Y)일 경우 처리 로직
        if(resign.getResignYn().equals("Y")){
            if(!userService.deleteUser(employeeNumber))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResUserVO(400, "No matched user", "/users/list", null)
                );

            ResUserVO response =
                    new ResUserVO(200, "Success to resign " + employeeNumber + " user",
                            "/users/list", null);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        //재입사(N)일 경우 처리 로직
        if(!userService.rejoinUser(employeeNumber))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResUserVO(400, "No matched user", "/users/list", null)
            );

        ResUserVO response =
                new ResUserVO(200, "Success to rejoin " + employeeNumber + " user",
                        "/users/list", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 비밀번호 찾기 ( 리셋 )
    @PutMapping("/pwd")
    public ResponseEntity<ResUserVO> resetPwd(@RequestBody ReqResetPwdVO info) {

        Map<String, String> infos = new HashMap<>();
        infos.put("email", info.getEmail());
        infos.put("pwd", info.getPwd());


        System.out.println("infos = " + infos);
        if (userService.resetPwd(infos)){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResUserVO(200, "Success to reset password", "/", null));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResUserVO(400, "Failed to reset password", "/", null));
    }

    @PostMapping("/duplication")
    public ResponseEntity<ResUserVO> duplicateInfoCheck(UserDTO givenInfo) {
        System.out.println("givenInfo = " + givenInfo);
        String result = userService.duplicateInfoCheck(givenInfo);
        if (!result.equals("Pass")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResUserVO(400,
                            userService.duplicateInfoCheck(givenInfo),
                            "/user", null ));
        }

        return null;
    }
}


package org.highfives.grid.user.query.controller;

import io.jsonwebtoken.Claims;
import org.highfives.grid.approval.query.dto.EmpStatusDTO;
import org.highfives.grid.approval.query.service.ApprovalService;
import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.user.query.vo.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("UserQueryController")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper,
                          ApprovalService approvalService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.approvalService = approvalService;
    }

    // 전체 직원 조회
    @GetMapping("/list/all")
    public ResponseEntity<ResFindListVO> findAllUsers() {
        String name = null;
        List<UserDTO> resultDTOs = userService.findList();
        List<SimpleInfo> resultList = DTOtoSimpleInfo(resultDTOs);

        ResFindListVO response =
                new ResFindListVO(200, "Success to find user list", "/users/{id}", resultList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 전체 직원 조회
    @GetMapping("/list")
    public ResponseEntity<ResFindListVO> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestAttribute("claims") Claims token) {

        String auth = (String) token.get("auth");
        Pageable pageable = PageRequest.of(page, size);
        List<EmpStatusDTO> absenceInfo = approvalService.findEmpStatus();

        Map<Integer, EmpStatusDTO> ndAbsenceInfo = absenceInfo.stream()
                .collect(Collectors.toMap(
                        EmpStatusDTO::getEmployeeId,
                        empStatus -> empStatus,
                        (existing, replacement) -> existing
                ));

        Page<UserDTO> resultDTOs = userService.findAllUsers(pageable, ndAbsenceInfo, auth);
        List<SimpleInfo> resultList = DTOtoSimpleInfo(resultDTOs.getContent());

        ResFindListVO response = new ResFindListVO(200, "Success to find user list", "/users/{id}", resultList,
                resultDTOs.getTotalElements(), resultDTOs.getTotalPages(), page);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 이름에 해당하는 직원 조회
    @GetMapping("/list/{name}")
    public ResponseEntity<ResFindListVO> findUsersByName(
            @PathVariable("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestAttribute("claims") Claims token) {

        String auth = (String) token.get("auth");
        Pageable pageable = PageRequest.of(page, size);
        List<EmpStatusDTO> absenceInfo = approvalService.findEmpStatus();

        Map<Integer, EmpStatusDTO> ndAbsenceInfo = absenceInfo.stream()
                .collect(Collectors.toMap(
                        EmpStatusDTO::getEmployeeId,
                        empStatus -> empStatus,
                        (existing, replacement) -> existing
                ));

        Page<UserDTO> resultDTOs = userService.findUsersByName(name, pageable, ndAbsenceInfo, auth);
        List<SimpleInfo> resultList = DTOtoSimpleInfo(resultDTOs.getContent());

        ResFindListVO response = new ResFindListVO(200, "Success to find user list", "/users/{id}", resultList,
                resultDTOs.getTotalElements(), resultDTOs.getTotalPages(), page);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 사번으로 직원 조회
    @GetMapping("/{employeeNumber}")
    public ResponseEntity<ResFindUserVO> findUserByEmployeeNum(@PathVariable("employeeNumber") String eNum) {

        List<EmpStatusDTO> absenceInfo = approvalService.findEmpStatus();

        Map<Integer, EmpStatusDTO> ndAbsenceInfo = absenceInfo.stream()
                .collect(Collectors.toMap(
                        EmpStatusDTO::getEmployeeId,
                        empStatus -> empStatus,
                        (existing, replacement) -> existing
                ));

        UserDTO userDTO = userService.findUserByEmployeeNum(eNum, ndAbsenceInfo);

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
        }

        ResFindUserVO response =
                new ResFindUserVO(404, "No matching user", "/users/{employeeNumber}", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //이메일로 사원 정보 조회
    @GetMapping("/mail/{email}")
    public ResponseEntity<ResFindUserVO> findUserByEmail(@PathVariable("email") String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        if(userDTO != null){
            ResFindUserVO response =
                    new ResFindUserVO(200, "Success to find user", "/users/list", userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        ResFindUserVO response =
                new ResFindUserVO(404, "No matching user", "/users/{employeeNumber}", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
    @GetMapping("/health")
    public String healthCheck() {

        return "Health check...Success to access?";
    }
}

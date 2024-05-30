package org.highfives.grid.department.query.controller;

import org.highfives.grid.department.query.dto.TeamDTO;
import org.highfives.grid.department.query.service.TeamService;
import org.highfives.grid.department.query.vo.ResponseTeamListVO;
import org.highfives.grid.department.query.vo.ResponseTeamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "queryTeamController")
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /* 설명. 팀 단일 조회 기능*/
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTeamVO> findTeamById(@PathVariable int id) {

        TeamDTO teamDTO = teamService.findTeamById(id);

        ResponseTeamVO responseTeamVO = ResponseTeamVO.builder()
                .href("team/{id}")
                .message("success")
                .statusCode(200)
                .result(teamDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseTeamVO);
    }

    /* 설명. 팀 전체 조회 기능 */
    @GetMapping("/find-all")
    public ResponseEntity<ResponseTeamListVO> findTeamList() {

        List<TeamDTO> teamDTO = teamService.findTeamList();

        ResponseTeamListVO responseTeamListVO = ResponseTeamListVO.builder()
                .href("team/find-all")
                .message("success")
                .statusCode(200)
                .result(teamDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseTeamListVO);
    }

    /* 설명. 하위 부서(팀) 소속 조회 */
    @GetMapping("/sub-department/{departmentId}")
    public ResponseEntity<ResponseTeamListVO> findSubDepartmentByDepartmentId(@PathVariable int departmentId) {


        List<TeamDTO> teamDTOList = teamService.findSubDepartmentByDepartmentId(departmentId);

        ResponseTeamListVO responseTeamListVO = ResponseTeamListVO.builder()
                .href("team/sub-department/{departmentId}")
                .message("success")
                .statusCode(200)
                .result(teamDTOList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseTeamListVO);
    }

}

package org.highfives.grid.department.command.controller;

import org.highfives.grid.department.command.dto.TeamDTO;
import org.highfives.grid.department.command.service.TeamService;
import org.highfives.grid.department.command.vo.ResponseDepartmentVO;
import org.highfives.grid.department.command.vo.ResponseTeamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "CommandTeamController")
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<ResponseTeamVO> addTeam(@RequestBody TeamDTO teamDTO) {

        TeamDTO registerData = teamService.addTeam(teamDTO);

        ResponseTeamVO responseTeamVO = ResponseTeamVO.builder()
                .message("Register success")
                .href("/team")
                .statusCode(201)
                .result(registerData)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(responseTeamVO);
    }

    @PutMapping
    public ResponseEntity<ResponseTeamVO> modifyTeam(@RequestBody TeamDTO teamDTO) {

        TeamDTO modifyData = teamService.modifyTeam(teamDTO);

        ResponseTeamVO responseTeamVO = ResponseTeamVO.builder()
                .message("modify success")
                .href("/team")
                .statusCode(201)
                .result(modifyData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseTeamVO);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable int id) {

        teamService.deleteTeam(id);

    }





}

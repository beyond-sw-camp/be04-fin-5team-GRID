package org.highfives.grid.user.query.dto;

import lombok.*;
import org.highfives.grid.user.command.aggregate.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class UserDTO {

    private int id;
    private String email;
    private String pwd;
    private String name;
    private String employeeNumber;
    private Gender gender;
    private String phoneNumber;
    private String callNumber;
    private int zipCode;
    private String address;
    private String assignedTask;
    private String joinTime;
    private JoinType joinType;
    private String resignTime;
    private YN resignYn;
    private WorkType workType;
    private String contractStartTime;
    private String contractEndTime;
    private int salary;
    private YN absenceYn;
    private String absenceContent;
    private String absenceStartTime;
    private String absenceEndTime;
    private Role role;
    private DepartmentDTO department;
    private TeamDTO team;
    private PositionDTO position;
    private DutiesDTO duties;
    private String profilePath;
    private String sealPath;

    public UserDTO(int id, String email, String pwd, String name, String employeeNumber, Gender gender,
                   String phoneNumber, String callNumber, int zipCode, String address, String assignedTask,
                   String joinTime, JoinType joinType, String resignTime, YN resignYn, WorkType workType,
                   String contractStartTime, String contractEndTime, int salary, YN absenceYn,
                   String absenceContent, String absenceStartTime, String absenceEndTime, Role role,
                   DepartmentDTO department, TeamDTO team, PositionDTO position, DutiesDTO duties,
                   String profilePath, String sealPath) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.callNumber = callNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.assignedTask = assignedTask;
        this.joinTime = joinTime;
        this.joinType = joinType;
        this.resignTime = resignTime;
        this.resignYn = resignYn;
        this.workType = workType;
        this.contractStartTime = contractStartTime;
        this.contractEndTime = contractEndTime;
        this.salary = salary;
        this.absenceYn = absenceYn;
        this.absenceContent = absenceContent;
        this.absenceStartTime = absenceStartTime;
        this.absenceEndTime = absenceEndTime;
        this.role = role;
        this.department = department;
        this.team = team;
        this.position = position;
        this.duties = duties;
        this.profilePath = profilePath;
        this.sealPath = sealPath;
    }
}

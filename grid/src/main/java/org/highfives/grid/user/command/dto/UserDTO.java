package org.highfives.grid.user.command.dto;

import lombok.*;
import org.highfives.grid.user.command.aggregate.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class UserDTO {

    private int id;
    private String email;
    private String pwd;
    private String name;
    private String employeeNumber;
    private Gender gender;
    private String phoneNumber;
    private String callNumber;
    private String zipCode;
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
    private Role role;
    private int dutiesId;
    private int positionId;
    private int teamId;
    private int departmentId;

    public UserDTO(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserDTO(String email, String pwd, String name, String employeeNumber, Gender gender,
                   String phoneNumber, String joinTime, JoinType joinType, WorkType workType,
                   String contractStartTime, Role role, int dutiesId, int positionId, int teamId, int departmentId) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.joinTime = joinTime;
        this.joinType = joinType;
        this.workType = workType;
        this.contractStartTime = contractStartTime;
        this.role = role;
        this.dutiesId = dutiesId;
        this.positionId = positionId;
        this.teamId = teamId;
        this.departmentId = departmentId;
    }

    public UserDTO(int id, String email, String pwd, String name, String employeeNumber, Gender gender,
                   String phoneNumber, String callNumber, String zipCode, String address,
                   String assignedTask, String joinTime, JoinType joinType, String resignTime, YN resignYn,
                   WorkType workType, String contractStartTime, String contractEndTime, int salary,
                   YN absenceYn, String absenceContent, Role role, int dutiesId, int positionId, int teamId,
                   int departmentId) {
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
        this.role = role;
        this.dutiesId = dutiesId;
        this.positionId = positionId;
        this.teamId = teamId;
        this.departmentId = departmentId;
    }
}

package org.highfives.grid.user.query.dto;

import lombok.*;
import org.highfives.grid.user.command.aggregate.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    private Role role;
    private int dutiesId;
    private String duties;
    private int positionId;
    private String position;
    private int teamId;
    private int departmentId;
    private String profilePath;
    private String sealPath;

    @Builder
    public UserDTO(int id, String email, String pwd, String name, String employeeNumber,
                   Gender gender, String phoneNumber, String callNumber, int zipCode, String address,
                   String assignedTask, String joinTime, JoinType joinType, String resignTime, YN resignYn,
                   WorkType workType, String contractStartTime, String contractEndTime, int salary, YN absenceYn,
                   String absenceContent, int dutiesId, String duties, int positionId, String position, Role role,
                   int teamId, int departmentId, String profilePath, String sealPath) {
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
        this.duties = duties;
        this.positionId = positionId;
        this.position = position;
        this.teamId = teamId;
        this.departmentId = departmentId;
        this.profilePath = profilePath;
        this.sealPath = sealPath;
    }

}

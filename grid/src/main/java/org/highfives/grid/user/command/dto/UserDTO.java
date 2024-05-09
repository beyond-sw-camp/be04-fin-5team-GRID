package org.highfives.grid.user.command.dto;

import lombok.*;
import org.highfives.grid.user.command.aggregate.Gender;
import org.highfives.grid.user.command.aggregate.JoinType;
import org.highfives.grid.user.command.aggregate.WorkType;
import org.highfives.grid.user.command.aggregate.YN;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class UserDTO {

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
    private int dutiesId;
    private int positionId;
    private int teamId;
    private int departmentId;

    public UserDTO(String email, String pwd, String name, String employeeNumber, Gender gender,
                    String phoneNumber, String joinTime, JoinType joinType, WorkType workType,
                    String contractStartTime, int dutiesId, int positionId, int teamId, int departmentId) {
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
        this.dutiesId = dutiesId;
        this.positionId = positionId;
        this.teamId = teamId;
        this.departmentId = departmentId;
    }
}
package org.highfives.grid.user.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.highfives.grid.user.command.aggregate.Gender;
import org.highfives.grid.user.command.aggregate.JoinType;
import org.highfives.grid.user.command.aggregate.WorkType;
import org.highfives.grid.user.command.aggregate.YN;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
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
    private String profilePath;
    private String sealPath;
}

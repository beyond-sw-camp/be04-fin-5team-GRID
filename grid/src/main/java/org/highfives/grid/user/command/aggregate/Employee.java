package org.highfives.grid.user.command.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="employee")
@Getter
@RequiredArgsConstructor
@ToString
public class Employee {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String email;
    @Column
    @Setter
    private String pwd;
    @Column
    private String employeeName;
    @Column
    private String employeeNumber;
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private String phoneNumber;
    @Column
    private String callNumber;
    @Column
    private String zipCode;
    @Column
    private String address;
    @Column
    private String assignedTask;
    @Column
    private String joinTime;
    @Column
    @Enumerated(EnumType.STRING)
    private JoinType joinType;
    @Column
    private String resignTime;
    @Column
    @Enumerated(EnumType.STRING)
    private YN resignYn;
    @Column
    @Enumerated(EnumType.STRING)
    private WorkType workType;
    @Column
    private String contractStartTime;
    @Column
    private String contractEndTime;
    @Column
    private int salary;
    @Column
    @Enumerated(EnumType.STRING)
    @Setter
    private YN absenceYn;
    @Column
    @Setter
    private String absenceContent;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private int dutiesId;
    @Column
    private int positionId;
    @Column
    private int teamId;
    @Column
    private int departmentId;

    public Employee(String email, String pwd, String employeeName, String employeeNumber, Gender gender,
                   String phoneNumber, String joinTime, JoinType joinType, WorkType workType,
                   String contractStartTime, int dutiesId, int positionId, int teamId, int departmentId) {
        this.email = email;
        this.pwd = pwd;
        this.employeeName = employeeName;
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

    @Builder
    public Employee(int id, String email, String pwd, String employeeName, String employeeNumber,
                    Gender gender, String phoneNumber, String callNumber, String zipCode, String address,
                    String assignedTask, String joinTime, JoinType joinType, String resignTime, YN resignYn,
                    WorkType workType, String contractStartTime, String contractEndTime, int salary,
                    YN absenceYn, String absenceContent, Role role,
                    int dutiesId, int positionId, int teamId, int departmentId) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.employeeName = employeeName;
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

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setResignTime(String resignTime) {
        this.resignTime = resignTime;
    }
    public void setResignYn(YN resignYn) {
        this.resignYn = resignYn;
    }

    public void setDutiesId(int dutiesId) {
        this.dutiesId = dutiesId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}

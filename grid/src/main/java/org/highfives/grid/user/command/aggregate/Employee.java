package org.highfives.grid.user.command.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
    private String pwd;
    @Column
    private String name;
    @Column
    private String employeeNumber;
    @Column
    private Gender gender;
    @Column
    private String phoneNumber;
    @Column
    private String callNumber;
    @Column(name = "zip_code")
    private String zipCode;
    @Column
    private String address;
    @Column(name = "assigned_task")
    private String assignedTask;
    @Column(name = "join_time")
    private String joinTime;
    @Column(name = "join_type")
    private JoinType joinType;
    @Column(name = "resign_time")
    private String resignTime;
    @Column(name = "resign_yn")
    private YN resignYn;
    @Column(name = "work_type")
    private WorkType workType;
    @Column(name = "contract_start_time")
    private String contractStartTime;
    @Column(name = "contract_end_time")
    private String contractEndTime;
    @Column
    private int salary;
    @Column(name = "absence_yn")
    private YN absenceYn;
    @Column(name = "absence_content")
    private String absenceContent;
    @Column(name = "duties_id")
    private int dutiesId;
    @Column(name = "position_id")
    private int positionId;
    @Column(name = "team_id")
    private int teamId;
    @Column(name = "department_id")
    private int departmentId;


    public Employee(String email, String pwd, String name, String employeeNumber, Gender gender,
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

package org.highfives.grid.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;


@Entity
@DynamicInsert
@Table(name = "employee")
@Getter
@RequiredArgsConstructor
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name= "pwd")
    private String pwd;

    @Column(nullable = false, name = "employee_name")
    private String employeeName;

    @Column(nullable = false, name = "employee_number")
    private String employeeNumber;

    @Column(nullable = false, name = "gender")
    private String gender;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(name = "call_number")
    private String callNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "assigned_task")
    private String assignedTask;

    @Column(nullable = false, name = "join_time")
    private String joinTime;

    @Column(nullable = false, name = "join_type")
    private String joinType;

    @Column(name = "resign_time")
    private String resignTime;

    @Column(name = "resign_yn")
    private String resignYn;

    @Column(nullable = false, name = "work_type")
    private String workType;

    @Column(nullable = false, name = "contract_start_time")
    private String contractStartTime;

    @Column(name = "contract_end_time")
    private String contractEndTime;

    @Column(nullable = false, name = "salary")
    private int salary;

    @Column(nullable = false, name = "absence_yn")
    private String absenceYn;

    @Column(name = "absence_content")
    private String absenceContent;

    @Column(nullable = false, name = "duties_id")
    private int dutiesId;

    @Column(nullable = false, name = "position_id")
    private int positionId;

    @Column(nullable = false, name = "team_id")
    private int teamId;

    @Column(nullable = false, name = "department_id")
    private int departmentId;

    @Builder
    public Employee(int id, String email, String pwd, String employeeName, String employeeNumber, String gender, String phoneNumber, String callNumber, String zipCode, String address, String assignedTask, String joinTime, String joinType, String resignTime, String resignYn, String workType, String contractStartTime, String contractEndTime, int salary, String absenceYn, String absenceContent, int dutiesId, int positionId, int teamId, int departmentId) {
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
        this.dutiesId = dutiesId;
        this.positionId = positionId;
        this.teamId = teamId;
        this.departmentId = departmentId;
    }
}

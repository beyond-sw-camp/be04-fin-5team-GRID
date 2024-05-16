package org.highfives.grid.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "employee")
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
    private int zipCode;

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
}

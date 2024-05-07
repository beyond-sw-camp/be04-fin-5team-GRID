package org.highfives.grid.department.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor
@ToString
public class Department {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @Column(name = "member_cnt", nullable = false)
    private int memberCnt;

    @Column(name = "department_status", length = 127, nullable = false)
    private String departmentStatus;

    @Column(name = "start_time", length = 127, nullable = false)
    private String startTime;

    @Column(name = "end_time", length = 127, nullable = false)
    private String endTime;

    @Column(name = "high_department", length = 127)
    private String highDepartment;

    @Column(name = "leader_id", nullable = false)
    private int leaderId;

}

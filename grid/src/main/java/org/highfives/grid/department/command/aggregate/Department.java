package org.highfives.grid.department.command.aggregate;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "department_name", length = 127, nullable = false)
    private String departmentName;

    @Column(name = "member_cnt", nullable = false)
    private int memberCnt;

    @Column(name = "department_status", length = 127, nullable = false)
    private String departmentStatus;

    @Column(name = "start_time", length = 127, nullable = false)
    private String startTime;

    @Column(name = "end_time", length = 127)
    private String endTime;


    @Column(name = "leader_id", nullable = false)
    @Setter
    private int leaderId;

    @Column(name = "department_code", nullable = false)
    private String departmentCode;

    @Column(name = "sequence")
    private int sequence;

    @Builder
    public Department(int id, String departmentName, int memberCnt, String departmentStatus,
                      String startTime, String endTime, int leaderId, String departmentCode, int sequence) {
        this.id = id;
        this.departmentName = departmentName;
        this.memberCnt = memberCnt;
        this.departmentStatus = departmentStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.leaderId = leaderId;
        this.departmentCode = departmentCode;
        this.sequence = sequence;
    }
}

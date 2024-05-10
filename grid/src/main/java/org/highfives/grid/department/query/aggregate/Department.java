package org.highfives.grid.department.query.aggregate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class Department {


    private int id;

    private String name;

    private int memberCnt;

    private String departmentStatus;

    private String startTime;

    private String endTime;

    private String highDepartment;

    private int leaderId;

    @Builder
    public Department(int id, String name, int memberCnt, String departmentStatus, String startTime, String endTime, String highDepartment, int leaderId) {
        this.id = id;
        this.name = name;
        this.memberCnt = memberCnt;
        this.departmentStatus = departmentStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.highDepartment = highDepartment;
        this.leaderId = leaderId;
    }
}

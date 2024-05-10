package org.highfives.grid.department.command.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentDTO {


    private int id;

    private String name;

    private int memberCnt;

    private String departmentStatus;

    private String startTime;

    private String endTime;

    private String highDepartment;

    private int leaderId;

    @Builder
    public DepartmentDTO(int id, String name, int memberCnt, String departmentStatus, String startTime, String endTime, String highDepartment, int leaderId) {
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

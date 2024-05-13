package org.highfives.grid.department.command.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentDTO {


    private int id;

    private String departmentName;

    private int memberCnt;

    private String departmentStatus;

    private String startTime;

    private String endTime;

    private String highDepartment;

    private int leaderId;

    private String departmentCode;

    @Builder
    public DepartmentDTO(int id, String departmentName, int memberCnt, String departmentStatus, String startTime,
                         String endTime, String highDepartment, int leaderId, String departmentCode) {
        this.id = id;
        this.departmentName = departmentName;
        this.memberCnt = memberCnt;
        this.departmentStatus = departmentStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.highDepartment = highDepartment;
        this.leaderId = leaderId;
        this.departmentCode = departmentCode;
    }
}

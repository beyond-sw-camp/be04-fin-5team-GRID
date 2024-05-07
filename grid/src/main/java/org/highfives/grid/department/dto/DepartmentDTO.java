package org.highfives.grid.department.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
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

}

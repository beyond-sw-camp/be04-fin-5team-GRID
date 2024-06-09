package org.highfives.grid.approval.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpStatusDTO {

    int employeeId;

    String status;

    String startTime;

    String endTime;
}

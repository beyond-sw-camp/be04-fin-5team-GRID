package org.highfives.grid.work.common.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdTimeDTO {
    private int id;
    private String startTime;
    private String endTime;
    private int employeeId;
    private String employeeName;
    private String employeeNumber;
    private String attendanceStatus;    // 출근 상태

    public AdTimeDTO(String startTime, int employeeId) {
        this.startTime = startTime;
        this.employeeId = employeeId;
    }

    public AdTimeDTO(int employeeId, String endTime) {
        this.employeeId = employeeId;
        this.endTime = endTime;
    }
}

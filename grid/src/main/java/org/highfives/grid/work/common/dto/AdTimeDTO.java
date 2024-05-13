package org.highfives.grid.work.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class AdTimeDTO {
    private int id;
    private String startTime;
    private String endTime;
    private int employeeId;

    public AdTimeDTO(String startTime, int employeeId) {
        this.startTime = startTime;
        this.employeeId = employeeId;
    }

    public AdTimeDTO(int employeeId, String endTime) {
        this.employeeId = employeeId;
        this.endTime = endTime;
    }
}

package org.highfives.grid.work.command.dto;

import lombok.*;


//@NoArgsConstructor
//@AllArgsConstructor
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

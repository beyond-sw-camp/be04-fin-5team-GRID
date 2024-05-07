package org.highfives.grid.work.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class QueryAdTimeDTO {
    private int id;

    private String startTime;

    private String endTime;

    private int employeeId;

    public QueryAdTimeDTO(String startTime, int employeeId) {
        this.startTime = startTime;
        this.employeeId = employeeId;
    }

    public QueryAdTimeDTO(int employeeId, String endTime) {
        this.employeeId = employeeId;
        this.endTime = endTime;
    }
}

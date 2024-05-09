package org.highfives.grid.work.query.aggregate;

import jakarta.persistence.*;
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
public class AdTime {

    private int id;
    private String startTime;
    private String endTime;
    private int employeeId;

    public AdTime(String startTime, int employeeId) {
        this.startTime = startTime;
        this.employeeId = employeeId;
    }

    public AdTime(int employeeId, String endTime) {
        this.employeeId = employeeId;
        this.endTime = endTime;
    }
}

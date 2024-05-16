package org.highfives.grid.vacation.query.entity;

import lombok.Data;

@Data
public class VacationInfo {

    private int id;
    private double vacationNum;
    private String addTime;
    private String endTime;
    private int employeeId;
    private int typeId;
}

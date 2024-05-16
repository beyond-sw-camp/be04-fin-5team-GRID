package org.highfives.grid.vacation.command.vo;

import lombok.Data;

@Data
public class GiveVacation {
    private double vacationNum;
    private String endTime;
    private int employeeId;
    private int typeId;

}

package org.highfives.grid.vacation.command.dto;

import lombok.Data;

@Data
public class VacationInfoDTO {
    private int id;
    private double vacationNum;
    private String addTime;
    private String endTime;
    private int employeeId;
    private int typeId;
}

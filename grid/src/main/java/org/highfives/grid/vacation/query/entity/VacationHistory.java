package org.highfives.grid.vacation.query.entity;

import lombok.Data;

@Data
public class VacationHistory {
    private int id;
    private String changeTime;
    private String changeReason;
    private int typeId;
    private int changeTypeId;
    private int employeeId;
}
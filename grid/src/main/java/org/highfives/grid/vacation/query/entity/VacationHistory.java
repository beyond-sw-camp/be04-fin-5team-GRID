package org.highfives.grid.vacation.query.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class VacationHistory {
    private int id;
    private String changeTime;
    private String changeReason;
    private int typeId;
    private int changeTypeId;
    private int employeeId;
    private double changeNum;
}

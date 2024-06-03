package org.highfives.grid.vacation.query.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class VacationInfo {

    private int id;
    private double vacationNum;
    private String addTime;
    private String endTime;
    private int employeeId;
    private int typeId;
    private String typeName;
    private String employeeName;
    private String employeeNumber;
}

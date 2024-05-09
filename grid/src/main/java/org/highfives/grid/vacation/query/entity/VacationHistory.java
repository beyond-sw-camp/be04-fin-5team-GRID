package org.highfives.grid.vacation.query.entity;

import lombok.Data;

@Data
public class VacationHistory {
    private int id;
    private String changeType;
    private String changeTime;
    private int typeId;
}

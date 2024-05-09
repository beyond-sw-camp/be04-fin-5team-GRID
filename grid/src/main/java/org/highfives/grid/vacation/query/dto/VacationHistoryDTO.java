package org.highfives.grid.vacation.query.dto;

import lombok.Data;

@Data
public class VacationHistoryDTO {
    private int id;
    private String changeTime;
    private String changeReason;
    private int typeId;
    private int changeTypeId;
    private int employeeId;
}

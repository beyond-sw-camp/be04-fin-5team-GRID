package org.highfives.grid.vacation.query.dto;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class VacationHistoryDTO {
    private int id;
    private String changeTime;
    private String changeReason;
    private int typeId;
    private int changeTypeId;
    private int employeeId;
    private String employeeName;
    private String typeName;
    private String changeTypeName;


}

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

    @Builder
    public VacationHistoryDTO(int id, String changeTime, String changeReason, int typeId, int changeTypeId, int employeeId) {
        this.id = id;
        this.changeTime = changeTime;
        this.changeReason = changeReason;
        this.typeId = typeId;
        this.changeTypeId = changeTypeId;
        this.employeeId = employeeId;
    }
}

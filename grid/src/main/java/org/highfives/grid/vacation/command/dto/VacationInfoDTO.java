package org.highfives.grid.vacation.command.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VacationInfoDTO {
    private int id;
    private double vacationNum;
    private String addTime;
    private String endTime;
    private int employeeId;
    private int typeId;

    @Builder
    public VacationInfoDTO(int id, double vacationNum, String addTime, String endTime, int employeeId, int typeId) {
        this.id = id;
        this.vacationNum = vacationNum;
        this.addTime = addTime;
        this.endTime = endTime;
        this.employeeId = employeeId;
        this.typeId = typeId;
    }
}

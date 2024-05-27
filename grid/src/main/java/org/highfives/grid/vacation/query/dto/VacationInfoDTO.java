package org.highfives.grid.vacation.query.dto;

import lombok.*;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class VacationInfoDTO {
    private int id;
    private double vacationNum;
    private String addTime;
    private String endTime;
    private int employeeId;
    private int typeId;
    private String typeName;
    private String employeeName;
    private String employeeNumber;

    @Builder
    public VacationInfoDTO(int id, double vacationNum, String addTime, String endTime, int employeeId, int typeId, String typeName, String employeeName, String employeeNumber) {
        this.id = id;
        this.vacationNum = vacationNum;
        this.addTime = addTime;
        this.endTime = endTime;
        this.employeeId = employeeId;
        this.typeId = typeId;
        this.typeName = typeName;
        this.employeeName = employeeName;
        this.employeeNumber = employeeNumber;
    }
}

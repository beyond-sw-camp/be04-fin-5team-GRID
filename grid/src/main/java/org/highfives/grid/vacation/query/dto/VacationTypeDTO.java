package org.highfives.grid.vacation.query.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class VacationTypeDTO {
    private int id;
    private String typeName;
    private String timeCheck;
    private int vacationTime;
    private String vacationNum;
    private String dateOfUse;
    private String vacationExplain;
    private String useYn;
}
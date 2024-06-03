package org.highfives.grid.vacation.query.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class VacationType {
    private int id;
    private String typeName;
    private String vacationNum;
    private String dateOfUse;
    private String vacationExplain;
    private String useYn;
}
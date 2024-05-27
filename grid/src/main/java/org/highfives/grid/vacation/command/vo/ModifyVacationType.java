package org.highfives.grid.vacation.command.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ModifyVacationType {
    private String vacationNum;
    private String dateOfUse;
    private String vacationExplain;
}

package org.highfives.grid.vacation.command.vo;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GiveVacation {
    private double vacationNum;
    private String endTime;
    private String employeeNum;
    private int employeeId;
    private int typeId;

}

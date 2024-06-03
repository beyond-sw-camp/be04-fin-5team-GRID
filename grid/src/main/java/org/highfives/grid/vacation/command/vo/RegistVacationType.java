package org.highfives.grid.vacation.command.vo;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RegistVacationType {
    private String typeName;
    private String vacationNum;
    private String dateOfUse;
    private String vacationExplain;
}
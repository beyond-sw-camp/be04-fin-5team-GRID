package org.highfives.grid.user.query.vo;

import lombok.*;
import org.highfives.grid.user.command.aggregate.YN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleInfo {
    private int id;
    private String profilePath;
    private String name;
    private String employeeNumber;
    private String department;
    private String team;
    private String position;
    private String duties;
    private String phoneNumber;
    private YN absenceYn;
    private String absenceContent;
    private String joinTime;
}

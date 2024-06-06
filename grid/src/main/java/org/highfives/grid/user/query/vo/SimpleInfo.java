package org.highfives.grid.user.query.vo;

import lombok.*;
import org.highfives.grid.user.command.aggregate.YN;
import org.highfives.grid.user.query.dto.DepartmentDTO;
import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.TeamDTO;

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
    private DepartmentDTO department;
    private TeamDTO team;
    private PositionDTO position;
    private DutiesDTO duties;
    private String phoneNumber;
    private YN absenceYn;
    private String absenceContent;
    private String joinTime;
}

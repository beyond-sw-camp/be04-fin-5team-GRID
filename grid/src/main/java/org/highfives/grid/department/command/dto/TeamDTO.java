package org.highfives.grid.department.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.highfives.grid.department.command.aggregate.TeamStatus;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamDTO {
    private int id;

    private String teamName;

    private int memberCnt;

    private String teamStatus;

    private TeamStatus startTime;

    private String endTime;

    private String departmentId;

    private String leaderId;
}

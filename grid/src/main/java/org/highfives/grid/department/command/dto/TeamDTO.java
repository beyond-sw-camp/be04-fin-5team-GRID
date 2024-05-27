package org.highfives.grid.department.command.dto;

import lombok.*;
import org.highfives.grid.department.command.aggregate.TeamStatus;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamDTO {
    private int id;

    private String teamName;

    private int memberCnt;

    private TeamStatus teamStatus;

    private String startTime;

    private String endTime;

    private int departmentId;

    private int leaderId;

    @Builder
    public TeamDTO(int id, String teamName, int memberCnt, TeamStatus teamStatus, String startTime, String endTime, int departmentId, int leaderId) {
        this.id = id;
        this.teamName = teamName;
        this.memberCnt = memberCnt;
        this.teamStatus = teamStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.departmentId = departmentId;
        this.leaderId = leaderId;
    }
}

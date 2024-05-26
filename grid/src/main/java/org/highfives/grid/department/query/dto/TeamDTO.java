package org.highfives.grid.department.query.dto;

import lombok.*;
import org.highfives.grid.department.query.aggregate.TeamStatus;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class TeamDTO {
    private int id;

    private String teamName;

    private int memberCnt;

    private String teamStatus;

    private TeamStatus startTime;

    private String endTime;

    private int departmentId;

    private int leaderId;

    @Builder
    public TeamDTO(int id, String teamName, int memberCnt, String teamStatus, TeamStatus startTime, String endTime, int departmentId, int leaderId) {
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

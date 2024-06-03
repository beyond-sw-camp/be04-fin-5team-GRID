package org.highfives.grid.department.query.aggregate;

import lombok.*;
import org.highfives.grid.department.query.aggregate.TeamStatus;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Team {
    private int id;

    private String teamName;

    private int memberCnt;

    private TeamStatus teamStatus;

    private String startTime;

    private String endTime;

    private int departmentId;

    private int leaderId;

    private int sequence;

    @Builder
    public Team(int id, String teamName, int memberCnt, TeamStatus teamStatus, String startTime, String endTime, int departmentId, int leaderId, int sequence) {
        this.id = id;
        this.teamName = teamName;
        this.memberCnt = memberCnt;
        this.teamStatus = teamStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.departmentId = departmentId;
        this.leaderId = leaderId;
        this.sequence = sequence;
    }
}

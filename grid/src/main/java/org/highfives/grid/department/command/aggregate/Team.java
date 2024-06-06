package org.highfives.grid.department.command.aggregate;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "team")
public class Team {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "member_cnt")
    private int memberCnt;

    @Column(name = "team_status")
    @Enumerated(EnumType.STRING)
    private TeamStatus teamStatus;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "leader_id")
    @Setter
    private int leaderId;

    @Column(name = "sequence")
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

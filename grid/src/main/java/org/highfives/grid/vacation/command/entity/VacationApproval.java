package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "vacation_approval")
public class VacationApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @Column(nullable = false, name = "start_time")
    private String startTime;

    @Column(nullable = false, name = "end_time")
    private String endTime;

    @Column(nullable = false, name="approval_status")
    private String approvalStatus;

    @Column(nullable = false, name="info_id")
    private int infoId;

    @Column(nullable = false, name="write_time")
    private String writeTime;

    @Column(nullable = false, name="cancel_yn")
    private String cancelYn;

    @Column(nullable = false, name="requester_id")
    private int requesterId;

}

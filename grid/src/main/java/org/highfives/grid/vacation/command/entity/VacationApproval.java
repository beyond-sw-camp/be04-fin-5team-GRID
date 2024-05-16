package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "vacation_approval")
@Getter
@ToString
@RequiredArgsConstructor
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

    @Builder
    public VacationApproval(int id, String content, String startTime, String endTime, String approvalStatus, int infoId, String writeTime, String cancelYn, int requesterId) {
        this.id = id;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.approvalStatus = approvalStatus;
        this.infoId = infoId;
        this.writeTime = writeTime;
        this.cancelYn = cancelYn;
        this.requesterId = requesterId;
    }
}

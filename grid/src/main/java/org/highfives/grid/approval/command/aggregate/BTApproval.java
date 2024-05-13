package org.highfives.grid.approval.command.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "bt_approval")
public class BTApproval {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "destination")
    private String destination;

    @Column(name = "content")
    private String content;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name = "write_time")
    private String writeTime;

    @Column(name = "cancel_yn")
    @Enumerated(EnumType.STRING)
    private YN cancelYN;

    @Column(name = "cancel_document_id")
    private int cancelDocId;

    @Column(name = "requester_id")
    private int reqeusterId;

    public BTApproval(String startTime, String endTime, String destination, String content, ApprovalStatus approvalStatus, String writeTime, YN cancelYN, int reqeusterId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.destination = destination;
        this.content = content;
        this.approvalStatus = approvalStatus;
        this.writeTime = writeTime;
        this.cancelYN = cancelYN;
        this.reqeusterId = reqeusterId;
    }
}

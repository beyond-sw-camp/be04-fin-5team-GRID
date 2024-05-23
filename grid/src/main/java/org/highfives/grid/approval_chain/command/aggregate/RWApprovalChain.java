package org.highfives.grid.approval_chain.command.aggregate;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "rw_approval_chain")
public class RWApprovalChain {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "approval_time")
    private String approvalTime;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ChainStatus approvalStatus;

    @Column(name = "comment")
    private String comment;

    @Column(name = "approval_id")
    private int approvalId;

    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "chain_id")
    private int chainId;

    @Builder
    public RWApprovalChain(ChainStatus approvalStatus, int approvalId, int employeeId, int chainId) {
        this.approvalStatus = approvalStatus;
        this.approvalId = approvalId;
        this.employeeId = employeeId;
        this.chainId = chainId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setApprovalStatus(ChainStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }
}

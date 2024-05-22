package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "v_approval_chain")
@Getter
@ToString
@RequiredArgsConstructor
public class VApprovalChain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "approval_time")
    private String approvalTime;

    @Column(nullable = false, name = "approval_status")
    private String approvalStatus;

    @Column(nullable = false, name = "comment")
    private String comment;

    @Column(nullable = false, name = "approval_id")
    private int approvalId;

    @Column(nullable = false, name = "employee_id")
    private int employeeId;

    @Column(nullable = false, name = "chain_id")
    private int chainId;

    @Builder
    public VApprovalChain(int id, String approvalTime, String approvalStatus, String comment, int approvalId, int employeeId, int chainId) {
        this.id = id;
        this.approvalTime = approvalTime;
        this.approvalStatus = approvalStatus;
        this.comment = comment;
        this.approvalId = approvalId;
        this.employeeId = employeeId;
        this.chainId = chainId;
    }
}

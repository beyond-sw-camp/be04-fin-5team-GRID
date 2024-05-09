package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "v_approval_chain")
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

}

package org.highfives.grid.approval_chain.command.aggregate;

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
@Table(name = "bt_approval_chain")
public class BTApprovalChain {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "approval_time")
    private String approvalTime;

    @Column(name = "approval_status")
    private ChainStatus approvalStatus;

    @Column(name = "comment")
    private String comment;

    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "chain_id")
    private int chainId;
}

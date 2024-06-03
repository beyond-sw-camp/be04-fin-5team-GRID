package org.highfives.grid.approval_chain.common.dto;

import lombok.*;
import org.highfives.grid.approval_chain.command.aggregate.ChainStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RWApprovalChainDTO {

    private int id;

    private String approvalTime;

    private ChainStatus approvalStatus;

    private String comment;

    private int approvalId;

    private int employeeId;

    private int chainId;
}

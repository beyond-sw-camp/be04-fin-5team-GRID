package org.highfives.grid.approval_chain.command.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReqAddApprovalChainVO {

    private int typeId;

    private int approvalId;

    private int employeeId;
}

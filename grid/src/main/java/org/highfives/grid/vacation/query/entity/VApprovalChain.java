package org.highfives.grid.vacation.query.entity;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VApprovalChain {
    private int id;
    private String approvalTime;
    private String approvalStatus;
    private String comment;
    private int approvalId;
    private int employeeId;
    private int chainId;
}

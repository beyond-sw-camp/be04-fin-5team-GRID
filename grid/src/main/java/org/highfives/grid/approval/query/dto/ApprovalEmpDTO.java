package org.highfives.grid.approval.query.dto;

import lombok.*;
import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.command.aggregate.YN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalEmpDTO {

    private int id;

    private String startTime;

    private String endTime;

    private String content;

    private ApprovalStatus approvalStatus;

    private String writeTime;

    private YN cancelYn;

    private int cancelDocId;

    private int requesterId;

    private String destination;

    private int infoId;

    private String employeeName;

    private int employeeNumber;
}


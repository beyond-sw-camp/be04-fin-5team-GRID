package org.highfives.grid.approval.common.dto;

import lombok.*;
import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.command.aggregate.YN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacationApprovalDTO {

    private int id;

    private String startTime;

    private String endTime;

    private String content;

    private ApprovalStatus approvalStatus;

    private String writeTime;

    private YN cancelYN;

    private int cancelDocId;

    private int requesterId;

    private int infoId;
}

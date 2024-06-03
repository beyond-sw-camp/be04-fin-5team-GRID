package org.highfives.grid.approval.command.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacationApprovalVO {

    private String startTime;

    private String endTime;

    private String content;

    private String writeTime;

    private int infoId;

    private int requesterId;
}

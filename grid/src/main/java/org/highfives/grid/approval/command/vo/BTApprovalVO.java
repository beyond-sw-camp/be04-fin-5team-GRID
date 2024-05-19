package org.highfives.grid.approval.command.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BTApprovalVO {

    private String startTime;

    private String endTime;

    private String destination;

    private String content;

    private String writeTime;

    private int requesterId;
}

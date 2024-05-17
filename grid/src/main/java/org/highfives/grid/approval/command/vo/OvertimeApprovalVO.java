package org.highfives.grid.approval.command.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OvertimeApprovalVO {

    private String startTime;

    private String endTime;

    private String content;

    private int typeId;

    private String writeTime;

    private int requesterId;
}

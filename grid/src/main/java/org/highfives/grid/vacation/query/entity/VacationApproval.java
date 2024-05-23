package org.highfives.grid.vacation.query.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class VacationApproval {
    private int id;
    private String content;
    private String startTime;
    private String endTime;
    private String approvalStatus;
    private int infoId;
    private String writeTime;
    private String cancelYn;
    private int requesterId;
}

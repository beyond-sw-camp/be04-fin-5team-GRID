package org.highfives.grid.work.query.dto;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class BtApprovalDTO {
    private int id;
    private String startTime;
    private String endTime;
    private String destination;
    private String content;
    private String approvalStatus;
    private String writeTime;
    private String cancelYn;
    private int requesterId;
    private int cancelDocumentId;
}

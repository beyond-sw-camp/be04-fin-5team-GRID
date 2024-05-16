package org.highfives.grid.approval.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class RwApprovalDTO {
    private int id;
    private String startTime;
    private String endTime;
    private String content;
    private String approvalStatus;
    private int requesterId;
    private String writeTime;
    private String cancelYn;
    private String originName;
    private String renameName;
    private int cancelDocumentId;
    private String path;
}

package org.highfives.grid.approval.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class RWApprovalDTO {

    private int id;

    private String startTime;

    private String endTime;

    private String content;

    private String approvalStatus;

    private String writeTime;

    private String cancelYn;

    private int cancelDocumentId;

    private int requesterId;

    private String originName;

    private String renameName;

    private String path;
}

package org.highfives.grid.work.query.aggregate;

import lombok.Data;

@Data
public class RwApproval {
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

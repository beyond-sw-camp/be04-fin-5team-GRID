package org.highfives.grid.work.query.dto;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class OvertimeApprovalDTO {

    private int id;
    private String startTime;
    private String endTime;
    private String content;
    private String approvalStatus;
    private int typeId;
    private String writeTime;
    private String cancelYn;
    private int requesterId;
    private int cancelDocumentId;

    @Builder
    public OvertimeApprovalDTO(int id, String startTime, String endTime, String content, String approvalStatus, int typeId, String writeTime, String cancelYn, int requesterId, int cancelDocumentId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.approvalStatus = approvalStatus;
        this.typeId = typeId;
        this.writeTime = writeTime;
        this.cancelYn = cancelYn;
        this.requesterId = requesterId;
        this.cancelDocumentId = cancelDocumentId;
    }
}

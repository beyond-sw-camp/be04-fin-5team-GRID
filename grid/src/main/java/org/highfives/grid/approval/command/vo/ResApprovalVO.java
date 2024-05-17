package org.highfives.grid.approval.command.vo;

import lombok.*;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResApprovalVO {

    private int statusCode;

    private String message;

    private String href;

    private BTApprovalDTO btResult;

    private OvertimeApprovalDTO overtimeResult;

    @Builder
    public ResApprovalVO(int statusCode, String message, String href, BTApprovalDTO btResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.btResult = btResult;
    }

    @Builder
    public ResApprovalVO(int statusCode, String message, String href, OvertimeApprovalDTO overtimeResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.overtimeResult = overtimeResult;
    }
}

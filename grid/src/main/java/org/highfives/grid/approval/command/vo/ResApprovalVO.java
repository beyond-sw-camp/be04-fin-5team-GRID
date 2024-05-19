package org.highfives.grid.approval.command.vo;

import lombok.*;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;

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

    private RWApprovalDTO rwResult;

    private VacationApprovalDTO vacationResult;

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

    @Builder
    public ResApprovalVO(int statusCode, String message, String href, RWApprovalDTO rwResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.rwResult = rwResult;
    }

    @Builder
    public ResApprovalVO(int statusCode, String message, String href, VacationApprovalDTO vacationResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.vacationResult = vacationResult;
    }
}

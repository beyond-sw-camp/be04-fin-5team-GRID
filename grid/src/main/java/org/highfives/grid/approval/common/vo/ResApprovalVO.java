package org.highfives.grid.approval.common.vo;

import lombok.*;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
import org.highfives.grid.approval.query.dto.EmpStatusDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResApprovalVO {

    private int statusCode;

    private String message;

    private String href;

    private boolean success;

    private BTApprovalDTO btResult;

    private OvertimeApprovalDTO overtimeResult;

    private RWApprovalDTO rwResult;

    private VacationApprovalDTO vacationResult;

    private ApprovalEmpDTO approvalEmpResult;

    private List<BTApprovalDTO> btResultList;

    private List<OvertimeApprovalDTO> overtimeResultList;

    private List<RWApprovalDTO> rwResultList;

    private List<VacationApprovalDTO> vacationResultList;

    private List<ApprovalEmpDTO> approvalEmpResultList;

    private List<EmpStatusDTO> empStatusResultList;
}

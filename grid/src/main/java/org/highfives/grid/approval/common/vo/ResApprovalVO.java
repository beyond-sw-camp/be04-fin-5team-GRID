package org.highfives.grid.approval.common.vo;

import lombok.*;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;

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

    private BTApprovalDTO btResult;

    private OvertimeApprovalDTO overtimeResult;

    private RWApprovalDTO rwResult;

    private VacationApprovalDTO vacationResult;

    private List<ApprovalEmpDTO> approvalEmpResultList;

    private List<BTApprovalDTO> btResultList;

    private List<OvertimeApprovalDTO> oResultList;

    private List<RWApprovalDTO> rwResultList;

    private List<VacationApprovalDTO> vResultList;
}

package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;

public interface PdfService {

    void exportToPDF(int typeId, int approvalId);
    void BTexportToPDF(BTApprovalDTO btApproval);
    void OexportToPDF(OvertimeApprovalDTO overtimeApproval);
    void RWexportToPDF(RWApprovalDTO rwApproval);
    void VexportToPDF(VacationApprovalDTO vacationApproval);
}

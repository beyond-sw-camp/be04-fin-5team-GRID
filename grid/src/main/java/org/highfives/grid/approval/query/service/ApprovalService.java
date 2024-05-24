package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;

import java.util.List;

public interface ApprovalService {

    public List<BTApprovalDTO> findAllBTApproval();
    public List<ApprovalEmpDTO> findAllApprovalByEmployeeId(int typeId, int employeeId);
    ApprovalEmpDTO findDetailByApprovalId(int typeId, int approvalId);
    int countOvertimeInWeek(OvertimeInWeekDTO overtimeInWeek);
    public void BTexportToPDF(BTApprovalDTO btApproval, String filePath);
}

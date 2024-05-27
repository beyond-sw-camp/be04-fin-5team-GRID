package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;

import java.util.List;

public interface ApprovalService {

    List<BTApprovalDTO> findAllBTApproval();
    List<ApprovalEmpDTO> findAllApprovalByEmployeeId(int typeId, int employeeId, int isApproval);
    ApprovalEmpDTO findDetailByApprovalId(int typeId, int approvalId);
    List<ApprovalEmpDTO> findAllApprovalByApproverId(int typeId, int approverId, int isApproval);
    int countOvertimeInWeek(OvertimeInWeekDTO overtimeInWeek);
    void BTexportToPDF(BTApprovalDTO btApproval, String filePath);
}

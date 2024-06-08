package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
import org.highfives.grid.approval.query.dto.EmpStatusDTO;

import java.util.List;

public interface ApprovalService {

    List<ApprovalEmpDTO> findAllBTApproval(int typeId, int isApproval);
    List<ApprovalEmpDTO> findAllApprovalByEmployeeId(int typeId, int isApproval, int employeeId);
    ApprovalEmpDTO findDetailByApprovalId(int typeId, int approvalId);
    List<ApprovalEmpDTO> findAllApprovalByApproverId(int typeId, int isApproval, int approverId);
    double countOvertimeInWeek(OvertimeInWeekDTO overtimeInWeek);
    List<ApprovalEmpDTO> findTodayBT();
    List<ApprovalEmpDTO> findTodayV();
    List<EmpStatusDTO> findEmpStatus();
}

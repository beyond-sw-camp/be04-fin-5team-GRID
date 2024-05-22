package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;

import java.util.List;

public interface ApprovalService {

    public BTApprovalDTO findBTApprovalById(int btApprovalId);
    List<OvertimeApprovalDTO> findOInWeekByEmployeeId(OvertimeInWeekDTO overtimeInWeek);
    int countOvertimeInWeek(List<OvertimeApprovalDTO> overtimeApprovalList);
    public void BTexportToPDF(BTApprovalDTO btApproval, String filePath);
}

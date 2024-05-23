package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;

import java.util.List;

public interface ApprovalService {

    public BTApprovalDTO findBTApprovalById(int btApprovalId);
    int countOvertimeInWeek(OvertimeInWeekDTO overtimeInWeek);
    public void BTexportToPDF(BTApprovalDTO btApproval, String filePath);
}

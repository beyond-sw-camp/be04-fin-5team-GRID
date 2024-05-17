package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;

public interface ApprovalService {

    BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO);
    OvertimeApprovalDTO addOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO);
    BTApprovalDTO modifyBTApproval(BTApprovalVO btApprovalVO, int btApprovalId);
    OvertimeApprovalDTO modifyOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO, int overtimeApprovalId);
    BTApprovalDTO cancelBTApproval(int btApprovalId);
    OvertimeApprovalDTO cancelOvertimeApproval(int overtimeApprovalId);
}

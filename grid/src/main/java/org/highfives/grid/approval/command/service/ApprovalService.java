package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
import org.highfives.grid.approval.command.vo.RWApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;

public interface ApprovalService {

    BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO);
    OvertimeApprovalDTO addOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO);
    RWApprovalDTO addRWApproval(RWApprovalVO rwApprovalVO);
    BTApprovalDTO modifyBTApproval(BTApprovalVO btApprovalVO, int btApprovalId);
    OvertimeApprovalDTO modifyOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO, int overtimeApprovalId);
    RWApprovalDTO modifyRWApproval(RWApprovalVO rwApprovalVO, int rwApprovalId);
    BTApprovalDTO cancelBTApproval(int btApprovalId);
    OvertimeApprovalDTO cancelOvertimeApproval(int overtimeApprovalId);
    RWApprovalDTO cancelRWApproval(int rwApprovalId);
}

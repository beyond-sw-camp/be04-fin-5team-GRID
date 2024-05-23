package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
import org.highfives.grid.approval.command.vo.RWApprovalVO;
import org.highfives.grid.approval.command.vo.VacationApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;

public interface ApprovalService {

    BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO);
    OvertimeApprovalDTO addOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO);
    RWApprovalDTO addRWApproval(RWApprovalVO rwApprovalVO);
    VacationApprovalDTO addVacationApproval(VacationApprovalVO vacationApprovalVO);
    BTApprovalDTO modifyBTApproval(BTApprovalVO btApprovalVO, int btApprovalId);
    OvertimeApprovalDTO modifyOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO, int overtimeApprovalId);
    RWApprovalDTO modifyRWApproval(RWApprovalVO rwApprovalVO, int rwApprovalId);
    VacationApprovalDTO modifyVacationApproval(VacationApprovalVO vacationApprovalVO, int vacationApprovalId);
    BTApprovalDTO cancelBTApproval(int btApprovalId);
    OvertimeApprovalDTO cancelOvertimeApproval(int overtimeApprovalId);
    RWApprovalDTO cancelRWApproval(int rwApprovalId);
    VacationApprovalDTO cancelVacationApproval(int vacationApprovalId);
    BTApprovalDTO viewBTApproval(int btApprovalId);
    OvertimeApprovalDTO viewOvertimeApproval(int overtimeApprovalId);
    RWApprovalDTO viewRWApproval(int rwApprovalId);
    VacationApprovalDTO viewVacationApproval(int vacationApprovalId);
}

package org.highfives.grid.work.query.service;

import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;

import java.util.List;

public interface OvertimeApprovalService {

    List<OvertimeApprovalDTO> findOvertimeAll();

    List<OvertimeApprovalDTO> findOvertimeByEmployeeId(int employeeId);

    List<OvertimeApprovalDTO> findOvertimeByOvertimeType();
}

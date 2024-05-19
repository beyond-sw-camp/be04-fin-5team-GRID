package org.highfives.grid.work.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;

import java.util.List;

public interface BtApprovalService {
    List<BTApprovalDTO> findAllBt();

    List<BTApprovalDTO> findBtByEmployeeId(int employeeId);
}

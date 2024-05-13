package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.BtApprovalDTO;

import java.util.List;

public interface BtApprovalService {
    List<BtApprovalDTO> findAllBt();

    List<BtApprovalDTO> findBtByEmployeeId(int employeeId);
}

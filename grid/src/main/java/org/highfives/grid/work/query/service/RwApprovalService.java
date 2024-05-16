package org.highfives.grid.work.query.service;

import org.highfives.grid.approval.common.dto.RwApprovalDTO;

import java.util.List;

public interface RwApprovalService {

    List<RwApprovalDTO> findAllRw();

    List<RwApprovalDTO> findRwByEmployeeId(int employeeId);
}

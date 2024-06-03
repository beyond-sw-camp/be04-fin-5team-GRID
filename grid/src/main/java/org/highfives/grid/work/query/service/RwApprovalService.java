package org.highfives.grid.work.query.service;

import org.highfives.grid.approval.common.dto.RWApprovalDTO;

import java.util.List;

public interface RwApprovalService {

    List<RWApprovalDTO> findAllRw();

    List<RWApprovalDTO> findRwByEmployeeId(int employeeId);
}

package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.aggregate.RwApproval;
import org.highfives.grid.work.query.dto.RwApprovalDTO;

import java.util.List;

public interface RwApprovalService {

    List<RwApprovalDTO> findAllRw();

    List<RwApprovalDTO> findRwByEmployeeId(int employeeId);
}

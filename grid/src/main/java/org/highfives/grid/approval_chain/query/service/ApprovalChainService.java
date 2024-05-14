package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;

import java.util.List;

public interface ApprovalChainService {

    List<ApprovalChainDTO> findChainListByTypeId(int approvalId);

    int findLeaderByEmployeeId(int employeeId, int chainId);
}
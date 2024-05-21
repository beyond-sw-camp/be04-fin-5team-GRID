package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.ChainDTO;

import java.util.List;
import java.util.Map;

public interface ApprovalChainService {

    List<ApprovalChainDTO> findChainListByTypeId(int approvalId);
    int findLeaderByEmployeeId(int employeeId, int chainId);
    ChainDTO findBTChainByApprovalAndChainId(int chainId, int approvalId);
    ChainDTO findOChainByApprovalAndChainId(int chainId, int approvalId);
}
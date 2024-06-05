package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.command.aggregate.OApprovalChain;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.ChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApprovalChainService {

    List<ApprovalChainDTO> findChainListByTypeId(int approvalId);
    int findLeaderByEmployeeId(int employeeId, int chainId);
    ChainDTO findBTChainByApprovalAndChainId(int typeId, int chainId, int approvalId);
    List<ChainDTO> findChainByApprovalId(int typeId, int approvalId);

}
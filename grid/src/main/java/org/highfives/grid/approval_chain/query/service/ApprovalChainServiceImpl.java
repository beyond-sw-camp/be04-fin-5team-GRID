package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.ChainDTO;
import org.highfives.grid.approval_chain.query.repository.ApprovalChainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "QueryApprovalChainService")
public class ApprovalChainServiceImpl implements ApprovalChainService {

    private final ApprovalChainMapper approvalChainMapper;

    @Autowired
    public ApprovalChainServiceImpl(ApprovalChainMapper approvalChainMapper) {
        this.approvalChainMapper = approvalChainMapper;
    }

    @Override
    public List<ApprovalChainDTO> findChainListByTypeId(int typeId) {

        List<ApprovalChainDTO> approvalChainList = approvalChainMapper.findChainListByTypeId(typeId);

        return approvalChainList;
    }

    @Override
    public int findLeaderByEmployeeId(int employeeId, int chainId) {

        int leaderId;

        if (chainId == 1) {
            leaderId = approvalChainMapper.findTeamLeaderByEmployeeId(employeeId);
        } else {
            leaderId = approvalChainMapper.findDeptLeaderByEmployeeId(employeeId);
        }

        return leaderId;
    }

    @Override
    public ChainDTO findBTChainByApprovalAndChainId(int chainId, int approvalId) {

        Map<String, Integer> params = new HashMap<>();

        params.put("chainId", chainId);
        params.put("approvalId", approvalId);

        ChainDTO chain = approvalChainMapper.findBTChainByApprovalAndChainId(params);

        return chain;
    }
}

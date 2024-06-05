package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.ChainDTO;
import org.highfives.grid.approval_chain.query.repository.ApprovalChainMapper;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "QueryApprovalChainService")
public class ApprovalChainServiceImpl implements ApprovalChainService {

    private final ApprovalChainMapper approvalChainMapper;
    private final UserService userService;

    @Autowired
    public ApprovalChainServiceImpl(ApprovalChainMapper approvalChainMapper, UserService userService) {
        this.approvalChainMapper = approvalChainMapper;
        this.userService = userService;
    }

    @Override
    public List<ApprovalChainDTO> findChainListByTypeId(int typeId) {

        return approvalChainMapper.findChainListByTypeId(typeId);
    }

    @Override
    public int findLeaderByEmployeeId(int employeeId, int chainId) {

        int leaderId;

        if (chainId == 1) {
            leaderId = approvalChainMapper.findDeptLeaderByEmployeeId(employeeId);
        } else {
            leaderId = approvalChainMapper.findTeamLeaderByEmployeeId(employeeId);
        }

        return leaderId;
    }

    @Override
    public ChainDTO findBTChainByApprovalAndChainId(int typeId, int chainId, int approvalId) {

        Map<String, Integer> params = new HashMap<>();

        params.put("typeId", typeId);
        params.put("chainId", chainId);
        params.put("approvalId", approvalId);

        return approvalChainMapper.findBTChainByApprovalAndChainId(params);
    }

    @Override
    public List<ChainDTO> findChainByApprovalId(int typeId, int approvalId) {

        Map<String, Integer> params = new HashMap<>();

        params.put("typeId", typeId);
        params.put("approvalId", approvalId);

        List<ChainDTO> chainList = approvalChainMapper.findChainByApprovalId(params);

        for (ChainDTO chain : chainList) {
            UserDTO user = userService.findUserById(chain.getEmployeeId());
            chain.setUser(user);
        }

        return chainList;
    }
}

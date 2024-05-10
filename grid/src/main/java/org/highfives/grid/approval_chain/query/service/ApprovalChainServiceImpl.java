package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.command.repository.ApprovalChainRepository;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.query.repository.ApprovalChainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalChainServiceImpl implements ApprovalChainService {

    private final ApprovalChainRepository approvalChainRepository;
    private final ApprovalChainMapper approvalChainMapper;

    @Autowired
    public ApprovalChainServiceImpl(ApprovalChainRepository approvalChainRepository, ApprovalChainMapper approvalChainMapper) {
        this.approvalChainRepository = approvalChainRepository;
        this.approvalChainMapper = approvalChainMapper;
    }

    @Override
    public List<ApprovalChainDTO> findChainListByApprovalId(int approvalId) {

        List<ApprovalChainDTO> approvalChainList = approvalChainMapper.findChainListByApprovalId(approvalId);

        return approvalChainList;
    }
}

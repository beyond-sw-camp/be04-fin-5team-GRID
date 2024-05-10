package org.highfives.grid.approval_chain.query.service;

import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;

import java.util.List;

public interface ApprovalChainService {

    List<ApprovalChainDTO> findChainListByApprovalId(int approvalId);
}
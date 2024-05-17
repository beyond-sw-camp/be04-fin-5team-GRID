package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.RWApprovaChainDTO;

import java.util.List;

public interface ApprovalChainService {

    List<BTApprovalChainDTO> addBTApprovalChain(ReqAddApprovalChainVO btChainVO);
    List<OApprovalChainDTO> addOApprovalChain(ReqAddApprovalChainVO overtimeChainVO);
    List<RWApprovaChainDTO> addRWApprovalChain(ReqAddApprovalChainVO rwChainVO);
}

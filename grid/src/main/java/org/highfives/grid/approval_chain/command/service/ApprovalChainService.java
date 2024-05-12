package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;

import java.util.List;

public interface ApprovalChainService {

    List<BTApprovalChainDTO> addBTApprovalChain(ReqAddApprovalChainVO btChainVO);
}

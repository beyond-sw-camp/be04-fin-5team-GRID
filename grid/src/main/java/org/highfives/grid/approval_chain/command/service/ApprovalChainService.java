package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval.command.vo.CommentVO;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.RWApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.VApprovalChainDTO;

import java.util.List;

public interface ApprovalChainService {

    List<BTApprovalChainDTO> addBTApprovalChain(ReqAddApprovalChainVO btChainVO);
    List<OApprovalChainDTO> addOApprovalChain(ReqAddApprovalChainVO overtimeChainVO);
    List<RWApprovalChainDTO> addRWApprovalChain(ReqAddApprovalChainVO rwChainVO);
    List<VApprovalChainDTO> addVApprovalChain(ReqAddApprovalChainVO vacationChainVO);
    BTApprovalChainDTO addBTApprovalComment(CommentVO commentVO);
    OApprovalChainDTO addOApprovalComment(CommentVO commentVO);
}

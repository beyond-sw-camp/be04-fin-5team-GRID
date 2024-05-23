package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval_chain.command.vo.ChainStatusVO;
import org.highfives.grid.approval_chain.command.vo.CommentVO;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.RWApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.VApprovalChainDTO;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface ApprovalChainService {

    List<BTApprovalChainDTO> addBTApprovalChain(ReqAddApprovalChainVO btChainVO);
    List<OApprovalChainDTO> addOApprovalChain(ReqAddApprovalChainVO overtimeChainVO);
    List<RWApprovalChainDTO> addRWApprovalChain(ReqAddApprovalChainVO rwChainVO);
    List<VApprovalChainDTO> addVApprovalChain(ReqAddApprovalChainVO vacationChainVO);
    BTApprovalChainDTO addBTApprovalComment(CommentVO commentVO);
    OApprovalChainDTO addOApprovalComment(CommentVO commentVO);
    RWApprovalChainDTO addRWApprovalComment(CommentVO commentVO);
    VApprovalChainDTO addVApprovalComment(CommentVO commentVO);
    BTApprovalChainDTO modifyBTChainStatus(ChainStatusVO chainStatusVO);
    BTApprovalDTO modifyBTApprovalStatus(int btApprovalId, ApprovalStatus approvalStatus);
    OApprovalChainDTO modifyOChainStatus(ChainStatusVO chainStatusVO);
    RWApprovalChainDTO modifyRWChainStatus(ChainStatusVO chainStatusVO);
    VApprovalChainDTO modifyVChainStatus(ChainStatusVO chainStatusVO);
}

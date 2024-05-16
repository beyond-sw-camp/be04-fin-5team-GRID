package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval_chain.command.aggregate.BTApprovalChain;
import org.highfives.grid.approval_chain.command.aggregate.ChainStatus;
import org.highfives.grid.approval_chain.command.repository.BTApprovalChainRepository;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "CommandApprovalChainService")
public class ApprovalChainServiceImpl implements ApprovalChainService{

    private final ModelMapper mapper;
    private final BTApprovalChainRepository btApprovalChainRepository;
    private final org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainServiceImpl(ModelMapper mapper, BTApprovalChainRepository btApprovalChainRepository, org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService) {
        this.mapper = mapper;
        this.btApprovalChainRepository = btApprovalChainRepository;
        this.approvalChainService = approvalChainService;
    }

    @Override
    @Transactional
    public List<BTApprovalChainDTO> addBTApprovalChain(ReqAddApprovalChainVO btChainVO) {

        int typeId = btChainVO.getTypeId();
        int employeeId = btChainVO.getEmployeeId();
        int approvalId = btChainVO.getApprovalId();

        List<ApprovalChainDTO> chainList = approvalChainService.findChainListByTypeId(typeId);
        List<BTApprovalChainDTO> btChainDTOList = new ArrayList<>();

        for (ApprovalChainDTO approvalChain : chainList) {
            int approverId = approvalChainService.findLeaderByEmployeeId(employeeId, approvalChain.getId());

            BTApprovalChain btApprovalChain = new BTApprovalChain(ChainStatus.W, approvalId, approverId, approvalChain.getId());
            btApprovalChainRepository.save(btApprovalChain);

            btChainDTOList.add(mapper.map(btApprovalChain, BTApprovalChainDTO.class));
        }

        return btChainDTOList;
    }
}

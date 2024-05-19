package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval_chain.command.aggregate.BTApprovalChain;
import org.highfives.grid.approval_chain.command.aggregate.ChainStatus;
import org.highfives.grid.approval_chain.command.aggregate.OApprovalChain;
import org.highfives.grid.approval_chain.command.aggregate.RWApprovalChain;
import org.highfives.grid.approval_chain.command.repository.BTApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.OApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.RWApprovalChainRepository;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.RWApprovalChainDTO;
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
    private final OApprovalChainRepository oApprovalChainRepository;
    private final RWApprovalChainRepository rwApprovalChainRepository;
    private final org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainServiceImpl(ModelMapper mapper, BTApprovalChainRepository btApprovalChainRepository, OApprovalChainRepository oApprovalChainRepository, RWApprovalChainRepository rwApprovalChainRepository, org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService) {
        this.mapper = mapper;
        this.btApprovalChainRepository = btApprovalChainRepository;
        this.oApprovalChainRepository = oApprovalChainRepository;
        this.rwApprovalChainRepository = rwApprovalChainRepository;
        this.approvalChainService = approvalChainService;
    }

    @Override
    @Transactional
    public List<BTApprovalChainDTO> addBTApprovalChain(ReqAddApprovalChainVO btChainVO) {

        List<ApprovalChainDTO> chainList = approvalChainService.findChainListByTypeId(btChainVO.getTypeId());
        List<BTApprovalChainDTO> btChainDTOList = new ArrayList<>();

        for (ApprovalChainDTO approvalChain : chainList) {
            int approverId = approvalChainService.findLeaderByEmployeeId(btChainVO.getEmployeeId(), approvalChain.getId());

            BTApprovalChain btApprovalChain = new BTApprovalChain(ChainStatus.W, btChainVO.getApprovalId(), approverId, approvalChain.getId());
            btApprovalChainRepository.save(btApprovalChain);

            btChainDTOList.add(mapper.map(btApprovalChain, BTApprovalChainDTO.class));
        }

        return btChainDTOList;
    }

    @Override
    @Transactional
    public List<OApprovalChainDTO> addOApprovalChain(ReqAddApprovalChainVO overtimeChainVO) {

        List<ApprovalChainDTO> chainList = approvalChainService.findChainListByTypeId(overtimeChainVO.getTypeId());
        List<OApprovalChainDTO> oChainDTOList = new ArrayList<>();

        for(ApprovalChainDTO approvalChain : chainList) {
            int approverId = approvalChainService.findLeaderByEmployeeId(overtimeChainVO.getEmployeeId(), approvalChain.getId());

            OApprovalChain oApprovalChain = OApprovalChain.builder()
                    .approvalStatus(ChainStatus.W)
                    .approvalId(overtimeChainVO.getApprovalId())
                    .employeeId(approverId)
                    .chainId(approvalChain.getId())
                    .build();

            oApprovalChainRepository.save(oApprovalChain);

            oChainDTOList.add(mapper.map(oApprovalChain, OApprovalChainDTO.class));
        }

        return oChainDTOList;
    }

    @Override
    @Transactional
    public List<RWApprovalChainDTO> addRWApprovalChain(ReqAddApprovalChainVO rwChainVO) {

        List<ApprovalChainDTO> chainList = approvalChainService.findChainListByTypeId(rwChainVO.getTypeId());
        List<RWApprovalChainDTO> rwChainDTOList = new ArrayList<>();

        for (ApprovalChainDTO approvalChain : chainList) {
            int approverId = approvalChainService.findLeaderByEmployeeId(rwChainVO.getEmployeeId(), approvalChain.getId());

            RWApprovalChain rwApprovalChain = RWApprovalChain.builder()
                    .approvalStatus(ChainStatus.W)
                    .approvalId(rwChainVO.getApprovalId())
                    .employeeId(approverId)
                    .chainId(approvalChain.getId())
                    .build();

            rwApprovalChainRepository.save(rwApprovalChain);

            rwChainDTOList.add(mapper.map(rwApprovalChain, RWApprovalChainDTO.class));
        }

        return rwChainDTOList;
    }
}

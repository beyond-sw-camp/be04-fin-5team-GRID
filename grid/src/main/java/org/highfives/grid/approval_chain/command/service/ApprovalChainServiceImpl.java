package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval_chain.command.vo.CommentVO;
import org.highfives.grid.approval_chain.command.aggregate.*;
import org.highfives.grid.approval_chain.command.repository.BTApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.OApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.RWApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.VApprovalChainRepository;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.common.dto.*;
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
    private final VApprovalChainRepository vApprovalChainRepository;
    private final org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalChainServiceImpl(ModelMapper mapper, BTApprovalChainRepository btApprovalChainRepository, OApprovalChainRepository oApprovalChainRepository, RWApprovalChainRepository rwApprovalChainRepository, VApprovalChainRepository vApprovalChainRepository, org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService) {
        this.mapper = mapper;
        this.btApprovalChainRepository = btApprovalChainRepository;
        this.oApprovalChainRepository = oApprovalChainRepository;
        this.rwApprovalChainRepository = rwApprovalChainRepository;
        this.vApprovalChainRepository = vApprovalChainRepository;
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

    @Override
    @Transactional
    public List<VApprovalChainDTO> addVApprovalChain(ReqAddApprovalChainVO vacationChainVO) {

        List<ApprovalChainDTO> chainList = approvalChainService.findChainListByTypeId(vacationChainVO.getTypeId());
        List<VApprovalChainDTO> vChainDTOList = new ArrayList<>();

        for (ApprovalChainDTO approvalChain : chainList) {
            int approverId = approvalChainService.findLeaderByEmployeeId(vacationChainVO.getEmployeeId(), approvalChain.getId());

            VApprovalChain vApprovalChain = VApprovalChain.builder()
                    .approvalStatus(ChainStatus.W)
                    .approvalId(vacationChainVO.getApprovalId())
                    .employeeId(approverId)
                    .chainId(approvalChain.getId())
                    .build();

            vApprovalChainRepository.save(vApprovalChain);

            vChainDTOList.add(mapper.map(vApprovalChain, VApprovalChainDTO.class));
        }

        return vChainDTOList;
    }

    @Override
    @Transactional
    public BTApprovalChainDTO addBTApprovalComment(CommentVO commentVO) {

        BTApprovalChain btApprovalChain = btApprovalChainRepository.findById(commentVO.getChainId()).orElseThrow();

        btApprovalChain.setComment(commentVO.getComment());

        btApprovalChainRepository.save(btApprovalChain);

        return mapper.map(btApprovalChain, BTApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public OApprovalChainDTO addOApprovalComment(CommentVO commentVO) {

        OApprovalChain oApprovalChain = oApprovalChainRepository.findById(commentVO.getChainId()).orElseThrow();

        oApprovalChain.setComment(commentVO.getComment());

        oApprovalChainRepository.save(oApprovalChain);

        return mapper.map(oApprovalChain, OApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public RWApprovalChainDTO addRWApprovalComment(CommentVO commentVO) {

        RWApprovalChain rwApprovalChain = rwApprovalChainRepository.findById(commentVO.getChainId()).orElseThrow();

        rwApprovalChain.setComment(commentVO.getComment());

        rwApprovalChainRepository.save(rwApprovalChain);

        return mapper.map(rwApprovalChain, RWApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public VApprovalChainDTO addVApprovalComment(CommentVO commentVO) {

        VApprovalChain vApprovalChain = vApprovalChainRepository.findById(commentVO.getChainId()).orElseThrow();

        vApprovalChain.setComment(vApprovalChain.getComment());

        vApprovalChainRepository.save(vApprovalChain);

        return mapper.map(vApprovalChain, VApprovalChainDTO.class);
    }
}

package org.highfives.grid.approval_chain.command.service;

import org.highfives.grid.approval.command.aggregate.*;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.command.repository.OApprovalRepository;
import org.highfives.grid.approval.command.repository.RWApprovalRepository;
import org.highfives.grid.approval.command.repository.VApprovalRepository;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval_chain.command.vo.CommentVO;
import org.highfives.grid.approval_chain.command.aggregate.*;
import org.highfives.grid.approval_chain.command.repository.BTApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.OApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.RWApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.VApprovalChainRepository;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.highfives.grid.approval_chain.command.vo.ChainStatusVO;
import org.highfives.grid.approval_chain.common.dto.*;
import org.highfives.grid.user.command.service.UserService;
import org.highfives.grid.vacation.command.service.VacationService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service(value = "CommandApprovalChainService")
public class ApprovalChainServiceImpl implements ApprovalChainService{

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ModelMapper mapper;
    private final BTApprovalChainRepository btApprovalChainRepository;
    private final BTApprovalRepository btApprovalRepository;
    private final OApprovalChainRepository oApprovalChainRepository;
    private final OApprovalRepository oApprovalRepository;
    private final RWApprovalChainRepository rwApprovalChainRepository;
    private final RWApprovalRepository rwApprovalRepository;
    private final VApprovalChainRepository vApprovalChainRepository;
    private final VApprovalRepository vApprovalRepository;
    private final org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService;
    private final UserService userService;
    private final VacationService vacationService;

    @Autowired
    public ApprovalChainServiceImpl(ModelMapper mapper, BTApprovalChainRepository btApprovalChainRepository, BTApprovalRepository btApprovalRepository, OApprovalChainRepository oApprovalChainRepository, OApprovalRepository oApprovalRepository, RWApprovalChainRepository rwApprovalChainRepository, RWApprovalRepository rwApprovalRepository, VApprovalChainRepository vApprovalChainRepository, VApprovalRepository vApprovalRepository, org.highfives.grid.approval_chain.query.service.ApprovalChainService approvalChainService, UserService userService, VacationService vacationService) {

        this.mapper = mapper;
        this.btApprovalChainRepository = btApprovalChainRepository;
        this.btApprovalRepository = btApprovalRepository;
        this.oApprovalChainRepository = oApprovalChainRepository;
        this.oApprovalRepository = oApprovalRepository;
        this.rwApprovalChainRepository = rwApprovalChainRepository;
        this.rwApprovalRepository = rwApprovalRepository;
        this.vApprovalChainRepository = vApprovalChainRepository;
        this.vApprovalRepository = vApprovalRepository;
        this.approvalChainService = approvalChainService;
        this.userService = userService;
        this.vacationService = vacationService;

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
        System.out.println(vApprovalChain);

        vApprovalChain.setComment(commentVO.getComment());

        vApprovalChainRepository.save(vApprovalChain);

        return mapper.map(vApprovalChain, VApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public BTApprovalChainDTO modifyBTChainStatus(ChainStatusVO chainStatusVO) {

        // 이미 approval status가 승인이나 반려 상태인 경우 -> 예외 처리
        // 이미 chain status가 승인이나 반려 상태인 경우 -> 예외 처리
        ChainDTO btChain = approvalChainService.findBTChainByApprovalAndChainId(1, chainStatusVO.getChainId(), chainStatusVO.getApprovalId());

        if (btChain.getStage() == 2) {  // 두번째 결재자
            ChainDTO btChain2 = approvalChainService.findBTChainByApprovalAndChainId(1, chainStatusVO.getChainId() + 1, chainStatusVO.getApprovalId());
            System.out.println(btChain2);
            if (btChain2.getChainStatus() == ChainStatus.A) {   // 첫번째 결재자가 승인한 경우에만 결재(승인/반려) 가능
                BTApprovalChain approvalChain = btApprovalChainRepository.findById(btChain.getId()).orElseThrow();

                approvalChain.setApprovalStatus(chainStatusVO.getChainStatus());
                approvalChain.setApprovalTime(LocalDateTime.now().format(dateFormat));

                btApprovalChainRepository.save(approvalChain);

                if (chainStatusVO.getChainStatus() == ChainStatus.A) {
                    // 결재 상태가 승인으로 변경되는 함수 호출
                    modifyBTApprovalStatus(btChain.getApprovalId(), ApprovalStatus.A);

                    BTApproval btApproval = btApprovalRepository.findById(chainStatusVO.getApprovalId()).orElseThrow();

                    if (btApproval.getCancelDocId() > 0) {

                        BTApproval canceledBTApproval = btApprovalRepository.findById(btApproval.getCancelDocId()).orElseThrow();
                        canceledBTApproval.setCancelYN(YN.Y);
                    }

                } else {
                    // 결재 상태가 반려로 변경되는 함수 호출
                    modifyBTApprovalStatus(btChain.getApprovalId(), ApprovalStatus.D);
                }

                return mapper.map(approvalChain, BTApprovalChainDTO.class);
            }

            return null;

            // 첫번째 결재자가 승인 상태가 아닌 경우 -> 예외 처리
        }

        BTApprovalChain approvalChain = btApprovalChainRepository.findById(btChain.getId()).orElseThrow();

        approvalChain.setApprovalStatus(chainStatusVO.getChainStatus());
        approvalChain.setApprovalTime(LocalDateTime.now().format(dateFormat));

        btApprovalChainRepository.save(approvalChain);

        if (chainStatusVO.getChainStatus() == ChainStatus.D) {
            // 결재 상태가 반려로 변경되는 함수 호출
            modifyBTApprovalStatus(btChain.getApprovalId(), ApprovalStatus.D);
        }

        return mapper.map(approvalChain, BTApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public BTApprovalDTO modifyBTApprovalStatus(int btApprovalId, ApprovalStatus approvalStatus) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();
        btApproval.setApprovalStatus(approvalStatus);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    @Transactional
    public OApprovalChainDTO modifyOChainStatus(ChainStatusVO chainStatusVO) {

        // 현재 승인 상태 체크에 사용
        OApprovalChain approvalChain = oApprovalChainRepository.findByApprovalId(chainStatusVO.getApprovalId());
        OvertimeApproval overtimeApproval = oApprovalRepository.findById(chainStatusVO.getApprovalId()).orElseThrow();

        approvalChain.setApprovalStatus(chainStatusVO.getChainStatus());
        approvalChain.setApprovalTime(LocalDateTime.now().format(dateFormat));

        if (chainStatusVO.getChainStatus() == ChainStatus.A) {
            overtimeApproval.setApprovalStatus(ApprovalStatus.A);

            if (overtimeApproval.getCancelDocId() > 0) {
                OvertimeApproval canceledOApproval = oApprovalRepository.findById(overtimeApproval.getCancelDocId()).orElseThrow();
                canceledOApproval.setCancelYN(YN.Y);
            }

        } else {
            // W는 예외 처리
            overtimeApproval.setApprovalStatus(ApprovalStatus.D);
        }

        oApprovalChainRepository.save(approvalChain);
        oApprovalRepository.save(overtimeApproval);

        return mapper.map(approvalChain, OApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public RWApprovalChainDTO modifyRWChainStatus(ChainStatusVO chainStatusVO) {

        RWApprovalChain approvalChain = rwApprovalChainRepository.findByApprovalId(chainStatusVO.getApprovalId());
        RWApproval rwApproval = rwApprovalRepository.findById(chainStatusVO.getApprovalId()).orElseThrow();

        approvalChain.setApprovalStatus(chainStatusVO.getChainStatus());
        approvalChain.setApprovalTime(LocalDateTime.now().format(dateFormat));

        if (chainStatusVO.getChainStatus() == ChainStatus.A) {
            rwApproval.setApprovalStatus(ApprovalStatus.A);

            userService.changeGender(rwApproval.getRequesterId());
            // 성별 남자의 경우 false 반환 -> 예외 처리

            if (rwApproval.getCancelDocId() > 0) {
                RWApproval canceledRWApproval = rwApprovalRepository.findById(rwApproval.getCancelDocId()).orElseThrow();
                canceledRWApproval.setCancelYN(YN.Y);

                userService.changeGender(canceledRWApproval.getRequesterId());
                // 성별 남자의 경우 false 반환 -> 예외 처리
            }

        } else {
            rwApproval.setApprovalStatus(ApprovalStatus.D);
        }

        rwApprovalChainRepository.save(approvalChain);
        rwApprovalRepository.save(rwApproval);

        return mapper.map(approvalChain, RWApprovalChainDTO.class);
    }

    @Override
    @Transactional
    public VApprovalChainDTO modifyVChainStatus(ChainStatusVO chainStatusVO) {

        VApprovalChain approvalChain = vApprovalChainRepository.findByApprovalId(chainStatusVO.getApprovalId());
        VacationApproval vacationApproval = vApprovalRepository.findById(chainStatusVO.getApprovalId()).orElseThrow();

        LocalDateTime startTime = LocalDateTime.parse(vacationApproval.getStartTime(), dateFormat);
        LocalDateTime endTime = LocalDateTime.parse(vacationApproval.getEndTime(), dateFormat);

        long businessDays = calculateBusinessDays(startTime, endTime);

        if (businessDays <= 1) {
            businessDays = 1;
        }

        approvalChain.setApprovalStatus(chainStatusVO.getChainStatus());
        approvalChain.setApprovalTime(LocalDateTime.now().format(dateFormat));

        if (chainStatusVO.getChainStatus() == ChainStatus.A) {
            vacationApproval.setApprovalStatus(ApprovalStatus.A);

            if(vacationApproval.getCancelDocId() > 0) {
                VacationApproval canceledVApproval = vApprovalRepository.findById(vacationApproval.getCancelDocId()).orElseThrow();
                canceledVApproval.setCancelYN(YN.Y);

                vacationService.plusVacationNum(vacationApproval.getRequesterId(), vacationApproval.getInfoId(), businessDays);

            } else {
                vacationService.minusVacationNum(vacationApproval.getRequesterId(), vacationApproval.getInfoId(), businessDays);
            }

        } else {
            vacationApproval.setApprovalStatus(ApprovalStatus.D);
        }

        vApprovalChainRepository.save(approvalChain);
        vApprovalRepository.save(vacationApproval);

        return mapper.map(approvalChain, VApprovalChainDTO.class);
    }

    public long calculateBusinessDays(LocalDateTime startDate, LocalDateTime endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // 시작일 포함
        long businessDays = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(date -> date.getDayOfWeek().getValue() < 6) // 주말 제외
                .count();
        return businessDays;
    }
}

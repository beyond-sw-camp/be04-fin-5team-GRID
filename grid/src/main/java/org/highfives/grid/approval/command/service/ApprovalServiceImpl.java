package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.aggregate.*;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.command.repository.OApprovalRepository;
import org.highfives.grid.approval.command.repository.RWApprovalRepository;
import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
import org.highfives.grid.approval.command.vo.RWApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval_chain.command.service.ApprovalChainService;
import org.highfives.grid.approval_chain.command.vo.ReqAddApprovalChainVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service(value = "CommandApprovalService")
public class ApprovalServiceImpl implements ApprovalService {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ModelMapper mapper;
    private final BTApprovalRepository btApprovalRepository;
    private final OApprovalRepository oApprovalRepository;
    private final RWApprovalRepository rwApprovalRepository;
    private final ApprovalChainService approvalChainService;

    @Autowired
    public ApprovalServiceImpl(ModelMapper mapper, BTApprovalRepository btApprovalRepository, OApprovalRepository oApprovalRepository, RWApprovalRepository rwApprovalRepository, ApprovalChainService approvalChainService) {
        this.mapper = mapper;
        this.btApprovalRepository = btApprovalRepository;
        this.oApprovalRepository = oApprovalRepository;
        this.rwApprovalRepository = rwApprovalRepository;
        this.approvalChainService = approvalChainService;
    }

    @Override
    @Transactional
    public BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO) {

        BTApproval btApproval = BTApproval.builder()
                .startTime(btApprovalVO.getStartTime())
                .endTime(btApprovalVO.getEndTime())
                .destination(btApprovalVO.getDestination())
                .content(btApprovalVO.getContent())
                .writeTime(LocalDateTime.now().format(dateFormat))
                .requesterId(btApprovalVO.getRequesterId())
                .build();

        btApprovalRepository.save(btApproval);

        ReqAddApprovalChainVO request = new ReqAddApprovalChainVO(1, btApproval.getId(), btApproval.getRequesterId());
        approvalChainService.addBTApprovalChain(request);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    @Transactional
    public OvertimeApprovalDTO addOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO) {

        String startTime = overtimeApprovalVO.getStartTime();
        String endTime = overtimeApprovalVO.getEndTime();

        String now = LocalDateTime.now().format(dateFormat);

        LocalDateTime startDate = LocalDateTime.parse(startTime, dateFormat);
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateFormat);

//        int startDay = startDate.toLocalDate().getDayOfWeek().getValue();
//        int endDay = endDate.toLocalDate().getDayOfWeek().getValue();
//
//        LocalTime startHour = startDate.toLocalTime();
//        LocalTime endHour = endDate.toLocalTime();
//
//         9시부터 6시까지 제외
//         start < end
//        if (startDay > 5 && endDay > 5) {
//            0시부터 6시 사이
//            startHour
//            22시부터 24시 사이
//            나머지
//        }

        OvertimeApproval overtimeApproval = OvertimeApproval.builder()
                .startTime(overtimeApprovalVO.getStartTime())
                .endTime(overtimeApprovalVO.getEndTime())
                .content(overtimeApprovalVO.getContent())
                .writeTime(LocalDateTime.now().format(dateFormat))
                .requesterId(overtimeApprovalVO.getRequesterId())
                .build();

        oApprovalRepository.save(overtimeApproval);

        ReqAddApprovalChainVO request = new ReqAddApprovalChainVO(2, overtimeApproval.getId(), overtimeApproval.getRequesterId());
        approvalChainService.addOApprovalChain(request);

        return mapper.map(overtimeApproval, OvertimeApprovalDTO.class);
    }

    @Override
    @Transactional
    public RWApprovalDTO addRWApproval(RWApprovalVO rwApprovalVO) {

        RWApproval rwApproval = RWApproval.builder()
                .startTime(rwApprovalVO.getStartTime())
                .endTime(rwApprovalVO.getEndTime())
                .content(rwApprovalVO.getContent())
                .writeTime(LocalDateTime.now().format(dateFormat))
                .requesterId(rwApprovalVO.getRequesterId())
                .originName(rwApprovalVO.getOriginName())
                .renameName(rwApprovalVO.getRenameName())
                .path(rwApprovalVO.getPath())
                .build();

        rwApprovalRepository.save(rwApproval);

        ReqAddApprovalChainVO request = new ReqAddApprovalChainVO(3, rwApproval.getId(), rwApproval.getRequesterId());
        approvalChainService.addRWApprovalChain(request);

        return mapper.map(rwApproval, RWApprovalDTO.class);
    }

    @Override
    @Transactional
    public BTApprovalDTO modifyBTApproval(BTApprovalVO btApprovalVO, int btApprovalId) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();

        if (btApproval.getApprovalStatus() == ApprovalStatus.N){
            btApproval.setStartTime(btApprovalVO.getStartTime());
            btApproval.setEndTime(btApprovalVO.getEndTime());
            btApproval.setDestination(btApprovalVO.getDestination());
            btApproval.setContent(btApprovalVO.getContent());
            btApproval.setWriteTime(btApprovalVO.getWriteTime());
        }

        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    @Transactional
    public OvertimeApprovalDTO modifyOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO, int overtimeApprovalId) {

        OvertimeApproval overtimeApproval = oApprovalRepository.findById(overtimeApprovalId).orElseThrow();

        if(overtimeApproval.getApprovalStatus() == ApprovalStatus.N) {
            overtimeApproval.setStartTime(overtimeApprovalVO.getStartTime());
            overtimeApproval.setEndTime(overtimeApprovalVO.getEndTime());
            overtimeApproval.setContent(overtimeApprovalVO.getContent());
            overtimeApproval.setWriteTime(overtimeApprovalVO.getWriteTime());
        }

        oApprovalRepository.save(overtimeApproval);

        return mapper.map(overtimeApproval, OvertimeApprovalDTO.class);
    }

    @Override
    @Transactional
    public RWApprovalDTO modifyRWApproval(RWApprovalVO rwApprovalVO, int rwApprovalId) {

        RWApproval rwApproval = rwApprovalRepository.findById(rwApprovalId).orElseThrow();

        if(rwApproval.getApprovalStatus() == ApprovalStatus.N) {
            rwApproval.setStartTime(rwApprovalVO.getStartTime());
            rwApproval.setEndTime(rwApprovalVO.getEndTime());
            rwApproval.setContent(rwApprovalVO.getContent());
            rwApproval.setOriginName(rwApprovalVO.getOriginName());
            rwApproval.setRenameName(rwApprovalVO.getRenameName());
            rwApproval.setPath(rwApprovalVO.getPath());
            rwApproval.setWriteTime(rwApprovalVO.getWriteTime());
        }

        rwApprovalRepository.save(rwApproval);

        return mapper.map(rwApproval, RWApprovalDTO.class);
    }

    @Override
    @Transactional
    public BTApprovalDTO cancelBTApproval(int btApprovalId) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();

        BTApproval cancelApproval = BTApproval.builder()
                .startTime(btApproval.getStartTime())
                .endTime(btApproval.getEndTime())
                .content(btApproval.getContent() + " \n취소")
                .destination(btApproval.getDestination())
                .writeTime(LocalDateTime.now().format(dateFormat))
                .requesterId(btApproval.getRequesterId())
                .cancelDocId(btApprovalId)
                .build();

        btApprovalRepository.save(cancelApproval);

        return mapper.map(cancelApproval, BTApprovalDTO.class);
    }

    @Override
    @Transactional
    public OvertimeApprovalDTO cancelOvertimeApproval(int overtimeApprovalId) {

        OvertimeApproval overtimeApproval = oApprovalRepository.findById(overtimeApprovalId).orElseThrow();

        OvertimeApproval cancelApproval = OvertimeApproval.builder()
                .startTime(overtimeApproval.getStartTime())
                .endTime(overtimeApproval.getEndTime())
                .content(overtimeApproval.getContent() + " \n취소")
                .writeTime(LocalDateTime.now().format(dateFormat))
                .cancelDocId(overtimeApprovalId)
                .requesterId(overtimeApproval.getRequesterId())
                .build();

        oApprovalRepository.save(cancelApproval);

        return mapper.map(cancelApproval, OvertimeApprovalDTO.class);
    }

    @Override
    @Transactional
    public RWApprovalDTO cancelRWApproval(int rwApprovalId) {

        RWApproval rwApproval = rwApprovalRepository.findById(rwApprovalId).orElseThrow();

        RWApproval cancelApproval = RWApproval.builder()
                .startTime(rwApproval.getStartTime())
                .endTime(rwApproval.getEndTime())
                .content(rwApproval.getContent() + " \n취소")
                .writeTime(LocalDateTime.now().format(dateFormat))
                .requesterId(rwApproval.getRequesterId())
                .originName(rwApproval.getOriginName())
                .renameName(rwApproval.getRenameName())
                .path(rwApproval.getPath())
                .cancelDocId(rwApprovalId)
                .build();

        rwApprovalRepository.save(cancelApproval);

        return mapper.map(cancelApproval, RWApprovalDTO.class);
    }
}

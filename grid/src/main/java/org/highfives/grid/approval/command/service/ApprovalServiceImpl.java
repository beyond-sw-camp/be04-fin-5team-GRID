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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service(value = "CommandApprovalService")
public class ApprovalServiceImpl implements ApprovalService {

    private final ModelMapper mapper;
    private final BTApprovalRepository btApprovalRepository;
    private final OApprovalRepository oApprovalRepository;
    private final RWApprovalRepository rwApprovalRepository;

    @Autowired
    public ApprovalServiceImpl(ModelMapper mapper, BTApprovalRepository btApprovalRepository, OApprovalRepository oApprovalRepository, RWApprovalRepository rwApprovalRepository) {
        this.mapper = mapper;
        this.btApprovalRepository = btApprovalRepository;
        this.oApprovalRepository = oApprovalRepository;
        this.rwApprovalRepository = rwApprovalRepository;
    }

    @Override
    @Transactional
    public BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO) {

        String startTime = btApprovalVO.getStartTime();
        String endTime = btApprovalVO.getEndTime();
        String destination = btApprovalVO.getDestination();
        String content = btApprovalVO.getContent();
        int requesterId = btApprovalVO.getRequesterId();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateFormat);

        BTApproval btApproval = new BTApproval(startTime, endTime, destination, content, now, 0, requesterId);
        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    @Transactional
    public OvertimeApprovalDTO addOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO) {

        String startTime = overtimeApprovalVO.getStartTime();
        String endTime = overtimeApprovalVO.getEndTime();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
                .writeTime(now)
                .requesterId(overtimeApprovalVO.getRequesterId())
                .build();

        oApprovalRepository.save(overtimeApproval);

        return mapper.map(overtimeApproval, OvertimeApprovalDTO.class);
    }

    @Override
    public RWApprovalDTO addRWApproval(RWApprovalVO rwApprovalVO) {

        String startTime = rwApprovalVO.getStartTime();
        String endTime = rwApprovalVO.getEndTime();
        String content = rwApprovalVO.getContent();
        int requesterId = rwApprovalVO.getRequesterId();

        String originName = rwApprovalVO.getOriginName();
        String renameName = rwApprovalVO.getRenameName();
        String path = rwApprovalVO.getPath();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateFormat);

        RWApproval rwApproval = RWApproval.builder()
                .startTime(rwApprovalVO.getStartTime())
                .endTime(rwApprovalVO.getEndTime())
                .content(rwApprovalVO.getContent())
                .writeTime(now)
                .requesterId(rwApprovalVO.getRequesterId())
                .originName(rwApprovalVO.getOriginName())
                .renameName(rwApprovalVO.getRenameName())
                .path(rwApprovalVO.getPath())
                .build();

        rwApprovalRepository.save(rwApproval);

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
        }

        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    public OvertimeApprovalDTO modifyOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO, int overtimeApprovalId) {

        OvertimeApproval overtimeApproval = oApprovalRepository.findById(overtimeApprovalId).orElseThrow();

        if(overtimeApproval.getApprovalStatus() == ApprovalStatus.N) {
            overtimeApproval.setStartTime(overtimeApprovalVO.getStartTime());
            overtimeApproval.setEndTime(overtimeApprovalVO.getEndTime());
            overtimeApproval.setContent(overtimeApprovalVO.getContent());
        }

        oApprovalRepository.save(overtimeApproval);

        return mapper.map(overtimeApproval, OvertimeApprovalDTO.class);
    }

    @Override
    public RWApprovalDTO modifyRWApproval(RWApprovalVO rwApprovalVO, int rwApprovalId) {

        RWApproval rwApproval = rwApprovalRepository.findById(rwApprovalId).orElseThrow();

        if(rwApproval.getApprovalStatus() == ApprovalStatus.N) {
            rwApproval.setStartTime(rwApprovalVO.getStartTime());
            rwApproval.setEndTime(rwApprovalVO.getEndTime());
            rwApproval.setContent(rwApprovalVO.getContent());
            rwApproval.setOriginName(rwApprovalVO.getOriginName());
            rwApproval.setRenameName(rwApprovalVO.getRenameName());
            rwApproval.setPath(rwApprovalVO.getPath());
        }

        rwApprovalRepository.save(rwApproval);

        return mapper.map(rwApproval, RWApprovalDTO.class);
    }

    @Override
    @Transactional
    public BTApprovalDTO cancelBTApproval(int btApprovalId) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateFormat);

        BTApproval cancelApproval = BTApproval.builder()
                .startTime(btApproval.getStartTime())
                .endTime(btApproval.getEndTime())
                .content(btApproval.getContent() + " \n취소")
                .destination(btApproval.getDestination())
                .writeTime(now)
                .requesterId(btApproval.getRequesterId())
                .cancelDocId(btApprovalId)
                .build();

        btApprovalRepository.save(cancelApproval);

        return mapper.map(cancelApproval, BTApprovalDTO.class);
    }

    @Override
    public OvertimeApprovalDTO cancelOvertimeApproval(int overtimeApprovalId) {

        OvertimeApproval overtimeApproval = oApprovalRepository.findById(overtimeApprovalId).orElseThrow();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateFormat);

        OvertimeApproval cancelApproval = OvertimeApproval.builder()
                .startTime(overtimeApproval.getStartTime())
                .endTime(overtimeApproval.getEndTime())
                .content(overtimeApproval.getContent() + " \n취소")
                .writeTime(now)
                .cancelDocId(overtimeApprovalId)
                .requesterId(overtimeApproval.getRequesterId())
                .build();

        oApprovalRepository.save(cancelApproval);

        return mapper.map(cancelApproval, OvertimeApprovalDTO.class);
    }

    @Override
    public RWApprovalDTO cancelRWApproval(int rwApprovalId) {

        RWApproval rwApproval = rwApprovalRepository.findById(rwApprovalId).orElseThrow();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateFormat);

        RWApproval cancelApproval = RWApproval.builder()
                .startTime(rwApproval.getStartTime())
                .endTime(rwApproval.getEndTime())
                .content(rwApproval.getContent() + " \n취소")
                .writeTime(now)
                .requesterId(rwApproval.getRequesterId())
                .originName(rwApproval.getOriginName())
                .renameName(rwApproval.getRenameName())
                .path(rwApproval.getPath())
                .cancelDocId(rwApprovalId)
                .build();

        rwApprovalRepository.save(cancelApproval);

        return mapper.map(rwApproval, RWApprovalDTO.class);
    }
}
package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.command.aggregate.OvertimeApproval;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "CommandApprovalService")
public class ApprovalServiceImpl implements ApprovalService {

    private final ModelMapper mapper;
    private final BTApprovalRepository btApprovalRepository;

    @Autowired
    public ApprovalServiceImpl(ModelMapper mapper, BTApprovalRepository btApprovalRepository) {
        this.mapper = mapper;
        this.btApprovalRepository = btApprovalRepository;
    }

    @Override
    @Transactional
    public BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO) {

        String startTime = btApprovalVO.getStartTime();
        String endTime = btApprovalVO.getEndTime();
        String destination = btApprovalVO.getDestination();
        String content = btApprovalVO.getContent();
        int requesterId = btApprovalVO.getRequesterId();

        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        BTApproval btApproval = new BTApproval(startTime, endTime, destination, content, now, 0, requesterId);
        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    public OvertimeApprovalDTO addOvertimeApproval(OvertimeApprovalVO overtimeApprovalVO) {

        String startTime = overtimeApprovalVO.getStartTime();
        String endTime = overtimeApprovalVO.getEndTime();
        String content = overtimeApprovalVO.getContent();
        int typeId = overtimeApprovalVO.getTypeId();
        int requesterId = overtimeApprovalVO.getRequesterId();

        OvertimeApproval overtimeApproval = new OvertimeApproval()

        return null;
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
    @Transactional
    public BTApprovalDTO cancelBTApproval(int btApprovalId) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();

        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        BTApproval cancelApproval = BTApproval.builder()
                .startTime(btApproval.getStartTime())
                .endTime(btApproval.getEndTime())
                .content(btApproval.getContent())
                .destination(btApproval.getDestination())
                .writeTime(now)
                .requesterId(btApproval.getRequesterId())
                .cancelDocId(btApprovalId)
                .build();

        btApprovalRepository.save(cancelApproval);

        return mapper.map(cancelApproval, BTApprovalDTO.class);
    }


}

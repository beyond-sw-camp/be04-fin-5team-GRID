package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.command.aggregate.YN;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.highfives.grid.user.command.aggregate.YN.N;

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

        BTApproval btApproval = new BTApproval(0, startTime, endTime, destination, content, ApprovalStatus.미열람, now, YN.N, -1, requesterId);
        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }
}

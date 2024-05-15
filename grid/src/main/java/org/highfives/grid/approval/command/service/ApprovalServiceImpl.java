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

        BTApproval btApproval = new BTApproval(startTime, endTime, destination, content, ApprovalStatus.N, now, YN.N, requesterId);
        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }

    @Override
    @Transactional
    public BTApprovalDTO modifyApproval(BTApprovalVO btApprovalVO, int btApprovalId) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();

        if (btApproval.getApprovalStatus() == ApprovalStatus.N){
            btApproval.builder()
                    .startTime(btApprovalVO.getStartTime())
                    .endTime(btApprovalVO.getEndTime())
                    .destination(btApprovalVO.getDestination())
                    .content(btApprovalVO.getContent())
                    .build();
        }

        btApprovalRepository.save(btApproval);

        return mapper.map(btApproval, BTApprovalDTO.class);
    }


}

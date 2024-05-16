package org.highfives.grid.work.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.work.query.repository.BtApprovalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value="QueryBtApprovalServiceImpl")
public class BtApprovalServiceImpl implements BtApprovalService{
    private final BtApprovalMapper btApprovalMapper;

    @Autowired
    public BtApprovalServiceImpl(BtApprovalMapper btApprovalMapper) {
        this.btApprovalMapper = btApprovalMapper;
    }

    @Override
    public List<BTApprovalDTO> findAllBt() {

        return btApprovalMapper.selectAllBt();
    }

    @Override
    public List<BTApprovalDTO> findBtByEmployeeId(int employeeId) {
        return btApprovalMapper.selectBtByEmployeeId();
    }
}

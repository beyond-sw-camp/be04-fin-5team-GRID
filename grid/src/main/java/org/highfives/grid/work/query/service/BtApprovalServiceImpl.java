package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.BtApprovalDTO;
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
    public List<BtApprovalDTO> findAllBt() {

        return btApprovalMapper.selectAllBt();
    }

    @Override
    public List<BtApprovalDTO> findBtByEmployeeId(int employeeId) {
        return btApprovalMapper.selectBtByEmployeeId();
    }
}

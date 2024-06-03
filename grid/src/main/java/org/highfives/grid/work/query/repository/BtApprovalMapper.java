package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;

import java.util.List;

@Mapper
public interface BtApprovalMapper {
    List<BTApprovalDTO> selectAllBt();

    List<BTApprovalDTO> selectBtByEmployeeId();
}

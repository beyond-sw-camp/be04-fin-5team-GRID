package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.work.query.dto.BtApprovalDTO;

import java.util.List;

@Mapper
public interface BtApprovalMapper {
    List<BtApprovalDTO> selectAllBt();

    List<BtApprovalDTO> selectBtByEmployeeId();
}

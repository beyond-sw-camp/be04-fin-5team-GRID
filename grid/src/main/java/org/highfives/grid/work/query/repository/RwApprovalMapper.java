package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.work.query.aggregate.RwApproval;

import java.util.List;

@Mapper
public interface RwApprovalMapper {
    List<RwApproval> selectAllRw();

    List<RwApproval> selectRwByEmployeeId(int employeeId);
}

package org.highfives.grid.approval_chain.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;

import java.util.List;

@Mapper
public interface ApprovalChainMapper {

    List<ApprovalChainDTO> findChainListByTypeId(int typeId);

    int findTeamLeaderByEmployeeId(int employeeId);

    int findDeptLeaderByEmployeeId(int employeeId);
}

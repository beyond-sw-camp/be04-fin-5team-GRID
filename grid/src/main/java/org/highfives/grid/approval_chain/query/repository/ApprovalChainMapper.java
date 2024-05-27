package org.highfives.grid.approval_chain.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval_chain.command.aggregate.ChainStatus;
import org.highfives.grid.approval_chain.common.dto.ApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.ChainDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApprovalChainMapper {

    List<ApprovalChainDTO> findChainListByTypeId(int typeId);
    int findTeamLeaderByEmployeeId(int employeeId);
    int findDeptLeaderByEmployeeId(int employeeId);
    ChainDTO findBTChainByApprovalAndChainId(Map<String, Integer> params);
    List<ChainDTO> findChainByApprovalId(Map<String, Integer> params);

}

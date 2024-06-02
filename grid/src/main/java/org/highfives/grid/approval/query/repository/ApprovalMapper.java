package org.highfives.grid.approval.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface ApprovalMapper {

    List<OvertimeApprovalDTO> findOInWeekByEmployeeId(OvertimeInWeekDTO overtimeInWeek);
    List<ApprovalEmpDTO> findAllBTApproval(Map<String, Integer> params);
    List<ApprovalEmpDTO> findAllBTApprovalByEmployeeId(Map<String, Integer> params);
    List<ApprovalEmpDTO> findAllOApprovalByEmployeeId(Map<String, Integer> params);
    List<ApprovalEmpDTO> findAllRWApprovalByEmployeeId(Map<String, Integer> params);
    List<ApprovalEmpDTO> findAllVApprovalByEmployeeId(Map<String, Integer> params);
    ApprovalEmpDTO findBTDetailByApprovalId(int approvalId);
    ApprovalEmpDTO findODetailByApprovalId(int approvalId);
    ApprovalEmpDTO findRWDetailByApprovalId(int approvalId);
    ApprovalEmpDTO findVDetailByApprovalId(int approvalId);
    List<ApprovalEmpDTO> findAllBTApprovalByApproverId(Map<String, Integer> params);
    List<ApprovalEmpDTO> findTodayBTandV(Map<String, Object> params);
}

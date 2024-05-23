package org.highfives.grid.approval.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;

import java.util.List;

@Mapper
public interface ApprovalMapper {

    List<OvertimeApprovalDTO> findOInWeekByEmployeeId(OvertimeInWeekDTO overtimeInWeek);
    List<BTApprovalDTO> findAllBTApproval();
    List<ApprovalEmpDTO> findAllBTApprovalByEmployeeId(int employeeId);
    List<ApprovalEmpDTO> findAllOvertimeApprovalByEmployeeId(int employeeId);
}

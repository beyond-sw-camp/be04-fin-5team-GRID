package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;

import java.util.List;

@Mapper
public interface OvertimeApprovalMapper {
    List<OvertimeApprovalDTO> selectOvertimeAll();

    List<OvertimeApprovalDTO> selectOvertimeByEmployeeId(int employeeId);
}

package org.highfives.grid.approval.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.approval.command.aggregate.OvertimeApproval;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;

import java.util.List;

@Mapper
public interface ApprovalMapper {

    List<OvertimeApprovalDTO> findOInWeekByEmployeeId(OvertimeInWeekDTO overtimeInWeek);
}

package org.highfives.grid.work.query.service;

import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.work.query.repository.OvertimeApprovalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvertimeApprovalServiceImpl implements OvertimeApprovalService{
    // 조회: 전체, 직원, 근무 유형, 주별 전체, 주별 직원, 부서, 직원이름, 사번, 문서 하나(결재라인까지)

    private final OvertimeApprovalMapper overtimeApprovalMapper;

    @Autowired
    public OvertimeApprovalServiceImpl(OvertimeApprovalMapper overtimeApprovalMapper) {
        this.overtimeApprovalMapper = overtimeApprovalMapper;
    }


    @Override
    public List<OvertimeApprovalDTO> findOvertimeAll() {

        return overtimeApprovalMapper.selectOvertimeAll();
    }

    @Override
    public List<OvertimeApprovalDTO> findOvertimeByEmployeeId(int employeeId) {

        return overtimeApprovalMapper.selectOvertimeByEmployeeId(employeeId);
    }

    @Override
    public List<OvertimeApprovalDTO> findOvertimeByOvertimeType() {
        return null;
    }
}

package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
import org.highfives.grid.approval.query.dto.EmpStatusDTO;
import org.highfives.grid.approval.query.repository.ApprovalMapper;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service(value = "QueryApprovalService")
public class ApprovalServiceImpl implements ApprovalService{

    private final ModelMapper mapper;
    private final ApprovalMapper approvalMapper;
    private final UserService userService;

    @Autowired
    public ApprovalServiceImpl(ModelMapper mapper, ApprovalMapper approvalMapper, UserService userService) {
        this.mapper = mapper;
        this.approvalMapper = approvalMapper;
        this.userService = userService;
    }

    @Override
    public List<ApprovalEmpDTO> findAllBTApproval(int typeId, int isApproval) {

        Map<String, Integer> params = new HashMap<>();

        params.put("typeId", typeId);
        params.put("isApproval", isApproval);

        return approvalMapper.findAllBTApproval(params);
    }

    @Override
    public List<ApprovalEmpDTO> findAllApprovalByEmployeeId(int typeId, int isApproval, int employeeId) {

        List<ApprovalEmpDTO> approvalEmpList = new ArrayList<>();
        Map<String, Integer> params = new HashMap<>();

        params.put("isApproval", isApproval);
        params.put("employeeId", employeeId);

        switch (typeId) {
            case 0:
                approvalEmpList.addAll(approvalMapper.findAllBTApprovalByEmployeeId(params));
                approvalEmpList.addAll(approvalMapper.findAllOApprovalByEmployeeId(params));
                approvalEmpList.addAll(approvalMapper.findAllRWApprovalByEmployeeId(params));
                approvalEmpList.addAll(approvalMapper.findAllVApprovalByEmployeeId(params));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                approvalEmpList.sort((emp1, emp2) -> {
                    LocalDateTime dateTime1 = LocalDateTime.parse(emp1.getWriteTime(), formatter);
                    LocalDateTime dateTime2 = LocalDateTime.parse(emp2.getWriteTime(), formatter);
                    return dateTime2.compareTo(dateTime1);
                });
                break;

            case 1:
                approvalEmpList = approvalMapper.findAllBTApprovalByEmployeeId(params);
                break;

            case 2:
                approvalEmpList = approvalMapper.findAllOApprovalByEmployeeId(params);
                break;

            case 3:
                approvalEmpList = approvalMapper.findAllRWApprovalByEmployeeId(params);
                break;

            case 4:
                approvalEmpList = approvalMapper.findAllVApprovalByEmployeeId(params);
        }

        return approvalEmpList;
    }

    @Override
    public ApprovalEmpDTO findDetailByApprovalId(int typeId, int approvalId) {

        ApprovalEmpDTO approvalEmp = new ApprovalEmpDTO();

        switch (typeId) {
            case 1:
                approvalEmp = approvalMapper.findBTDetailByApprovalId(approvalId);
                break;

            case 2:
                approvalEmp = approvalMapper.findODetailByApprovalId(approvalId);
                break;

            case 3:
                approvalEmp = approvalMapper.findRWDetailByApprovalId(approvalId);
                break;

            case 4:
                approvalEmp = approvalMapper.findVDetailByApprovalId(approvalId);
                break;
        }

        UserDTO user = userService.findUserById(approvalEmp.getEmployeeId());
        approvalEmp.setUser(user);

        return approvalEmp;
    }

    @Override
    public List<ApprovalEmpDTO> findAllApprovalByApproverId(int typeId, int isApproval, int approverId) {

        Map<String, Integer> params = new HashMap<>();

        if (typeId == 0) {
            List<ApprovalEmpDTO> approvalEmpList = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                params.put("typeId", i + 1);
                params.put("isApproval", isApproval);
                params.put("approverId", approverId);

                approvalEmpList.addAll(approvalMapper.findAllBTApprovalByApproverId(params));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            approvalEmpList.sort((emp1, emp2) -> {
                LocalDateTime dateTime1 = LocalDateTime.parse(emp1.getWriteTime(), formatter);
                LocalDateTime dateTime2 = LocalDateTime.parse(emp2.getWriteTime(), formatter);
                return dateTime2.compareTo(dateTime1);
            });

            return approvalEmpList;
        }

        params.put("typeId", typeId);
        params.put("isApproval", isApproval);
        params.put("approverId", approverId);

        return approvalMapper.findAllBTApprovalByApproverId(params);
    }

    @Override
    public double countOvertimeInWeek(OvertimeInWeekDTO overtimeInWeek) {

        List<OvertimeApprovalDTO> overtimeApprovalList = approvalMapper.findOInWeekByEmployeeId(overtimeInWeek);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        double sum = 0;

        for (OvertimeApprovalDTO overtimeApproval : overtimeApprovalList) {
            LocalDateTime startTime = LocalDateTime.parse(overtimeApproval.getStartTime(), dateFormat);
            LocalDateTime endTime = LocalDateTime.parse(overtimeApproval.getEndTime(), dateFormat);

            sum += ChronoUnit.MINUTES.between(startTime, endTime)/60.0;
        }

        return sum;
    }

    @Override
    public List<ApprovalEmpDTO> findTodayBT() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String today = LocalDateTime.now().format(dateFormat);

        Map<String, Object> params = new HashMap<>();

        params.put("typeId", 1);
        params.put("today", today);

        List<ApprovalEmpDTO> approvalEmpList = approvalMapper.findTodayBTandV(params);
        System.out.println(approvalEmpList);

        return approvalEmpList;
    }

    @Override
    public List<ApprovalEmpDTO> findTodayV() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String today = LocalDateTime.now().format(dateFormat);

        Map<String, Object> params = new HashMap<>();

        params.put("typeId", 2);
        params.put("today", today);

        List<ApprovalEmpDTO> approvalEmpList = approvalMapper.findTodayBTandV(params);

        return approvalEmpList;
    }

    @Override
    public List<EmpStatusDTO> findEmpStatus() {

        List<EmpStatusDTO> todayWork = new ArrayList<>();

        for (ApprovalEmpDTO bt : findTodayBT()) {
            todayWork.add(new EmpStatusDTO(bt.getEmployeeId(), "출장 중", bt.getStartTime(), bt.getEndTime()));
        }

        for (ApprovalEmpDTO v : findTodayV()) {
            todayWork.add(new EmpStatusDTO(v.getEmployeeId(), "휴가 중", v.getStartTime(), v.getEndTime()));
        }

        return todayWork;
    }
}

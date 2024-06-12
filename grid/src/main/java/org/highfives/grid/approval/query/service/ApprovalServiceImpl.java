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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        System.out.println("test " + approvalEmpList);

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
        DateTimeFormatter inputFormatBT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter inputFormatV = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yy.MM.dd");

        for (ApprovalEmpDTO bt : findTodayBT()) {
            try {
                LocalDate startDate = LocalDate.parse(bt.getStartTime(), inputFormatBT);
                LocalDate endDate = LocalDate.parse(bt.getEndTime(), inputFormatBT);
                String startTime = startDate.format(outputFormat);
                String endTime = endDate.format(outputFormat);
                todayWork.add(new EmpStatusDTO(bt.getEmployeeId(), "출장", startTime, endTime));
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date for BT: " + bt + ". Error: " + e.getMessage());
            }
        }

        for (ApprovalEmpDTO v : findTodayV()) {
            try {
                LocalDateTime startDateTime = LocalDateTime.parse(v.getStartTime(), inputFormatV);
                LocalDateTime endDateTime = LocalDateTime.parse(v.getEndTime(), inputFormatV);
                String startTime = startDateTime.format(outputFormat);
                String endTime = endDateTime.format(outputFormat);
                todayWork.add(new EmpStatusDTO(v.getEmployeeId(), "휴가", startTime, endTime));
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date for V: " + v + ". Error: " + e.getMessage());
            }
        }

        return todayWork;
    }


}

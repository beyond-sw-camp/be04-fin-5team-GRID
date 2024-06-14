//package org.highfives.grid.approval.query.service;
//
//import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
//import org.highfives.grid.approval.command.aggregate.BTApproval;
//import org.highfives.grid.approval.command.aggregate.YN;
//import org.highfives.grid.approval.command.repository.BTApprovalRepository;
//import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
//import org.highfives.grid.approval.query.repository.ApprovalMapper;
//import org.highfives.grid.user.query.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
////@ExtendWith(MockitoExtension.class)
//@Transactional
//class ApprovalServiceImplTest {
//
//    private final ModelMapper mapper;
//    private final ApprovalMapper approvalMapper;
//    private final UserService userService;
//
//    @Autowired
//    public ApprovalServiceImplTest(ModelMapper mapper, ApprovalMapper approvalMapper, UserService userService) {
//        this.mapper = mapper;
//        this.approvalMapper = approvalMapper;
//        this.userService = userService;
//    }
//
////    @Mock
////    private ApprovalMapper approvalMapper;
////
////    @Mock
////    private UserService userService;
////
////    @Mock
////    private BTApprovalRepository btApprovalRepository;
////
////    @InjectMocks
////    private ApprovalServiceImpl approvalService;
//
//    private ApprovalEmpDTO approvalEmpDTO;
//    private List<ApprovalEmpDTO> approvalEmpList;
//    private BTApproval btApproval;
//
//    @BeforeEach
//    void setUp() {
//        ApprovalEmpDTO approval1 = new ApprovalEmpDTO();
//
//        approval1.setStartTime("2024-06-11 09:00:00");
//        approval1.setEndTime("2024-06-11 17:00:00");
//        approval1.setDestination("대전");
//        approval1.setContent("프로젝트 관련 미팅 및 보고");
//        approval1.setUser(userService.findUserById(1));
//
//        ApprovalEmpDTO approval2 = new ApprovalEmpDTO();
//
//        approval2.setStartTime("2024-06-12 09:00:00");
//        approval2.setEndTime("2024-06-12 17:00:00");
//        approval2.setDestination("서울");
//        approval2.setContent("팀 회의 및 보고");
//        approval2.setUser(userService.findUserById(2));
//
//        approvalEmpList = Arrays.asList(approval1, approval2);
//
//        BTApproval btApproval1 = new BTApproval();
//
//        btApproval1.setStartTime("2024-06-11 09:00:00");
//        btApproval1.setEndTime("2024-06-11 17:00:00");
//        btApproval1.setDestination("대전");
//        btApproval1.setContent("프로젝트 관련 미팅 및 보고");
//
//        BTApproval btApproval2 = new BTApproval();
//
//        btApproval2.setStartTime("2024-06-12 09:00:00");
//        btApproval2.setEndTime("2024-06-12 17:00:00");
//        btApproval2.setDestination("서울");
//        btApproval2.setContent("팀 회의 및 보고");
//
////        btApprovalRepository.save(btApproval1);
////        btApprovalRepository.save(btApproval2);
//    }
//
//    @Test
//    void findAllBTApproval() {
//
//        Map<String, Integer> params = new HashMap<>();
//
//        params.put("typeId", 1);
//        params.put("isApproval", 0);
//
//        List<ApprovalEmpDTO> result = approvalMapper.findAllBTApproval(params);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void findAllApprovalByEmployeeId() {
//
//        Map<String, Integer> params = new HashMap<>();
//
//        params.put("typeId", 1);
//        params.put("employeeId", 1);
//
//        List<ApprovalEmpDTO> result = approvalMapper.findAllBTApproval(params);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void findDetailByApprovalId() {
//
//        int approvalId = 1;
//
//        ApprovalEmpDTO result = approvalMapper.findBTDetailByApprovalId(approvalId);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void findAllApprovalByApproverId() {
//
//        Map<String, Integer> params = new HashMap<>();
//
//        params.put("typeId", 1);
//        params.put("isApproval", 0);
//        params.put("approverId", 2);
//
//        List<ApprovalEmpDTO> result = approvalMapper.findAllBTApprovalByApproverId(params);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void findTodayBT() {
//
//        Map<String, Object> params = new HashMap<>();
//
//        params.put("typeId", 1);
//        params.put("today", "2024-06-07");
//
//        List<ApprovalEmpDTO> result = approvalMapper.findTodayBTandV(params);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void findTodayV() {
//
//        Map<String, Object> params = new HashMap<>();
//
//        params.put("typeId", 2);
//        params.put("today", "2024-06-07");
//
//        List<ApprovalEmpDTO> result = approvalMapper.findTodayBTandV(params);
//
//        assertNotNull(result);
//    }
//}
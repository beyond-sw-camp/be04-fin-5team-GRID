//package org.highfives.grid.approval.command.service;
//
//import org.highfives.grid.approval.command.aggregate.*;
//import org.highfives.grid.approval.command.repository.OApprovalRepository;
//import org.highfives.grid.approval.command.repository.RWApprovalRepository;
//import org.highfives.grid.approval.command.repository.VApprovalRepository;
//import org.highfives.grid.approval.command.vo.BTApprovalVO;
//import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
//import org.highfives.grid.approval.command.vo.RWApprovalVO;
//import org.highfives.grid.approval.command.vo.VacationApprovalVO;
//import org.highfives.grid.approval.common.dto.BTApprovalDTO;
//import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
//import org.highfives.grid.approval.common.dto.RWApprovalDTO;
//import org.highfives.grid.approval.common.dto.VacationApprovalDTO;
//
//import org.highfives.grid.approval.command.repository.BTApprovalRepository;
//
//import org.highfives.grid.approval.query.service.ApprovalService;
//import org.highfives.grid.approval_chain.command.service.ApprovalChainService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.internal.matchers.Equals;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.convention.MatchingStrategies;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ApprovalServiceTest {
//    @Spy
//    private ModelMapper mapper;
//
//    @Mock
//    private ApprovalChainService approvalChainService;
//
//    @Mock
//    private ApprovalService approvalQueryService;
//
//    @InjectMocks
//    private ApprovalServiceImpl approvalService;
//
//    @Mock
//    private BTApprovalRepository btApprovalRepository;
//
//    @Mock
//    private OApprovalRepository oApprovalRepository;
//
//    @Mock
//    private RWApprovalRepository rwApprovalRepository;
//
//    @Mock
//    private VApprovalRepository vApprovalRepository;
//
//
//    private BTApprovalVO btApprovalVO;
//    private BTApproval btApproval;
//    private BTApprovalDTO btApprovalDTO;
//
//    private OvertimeApprovalVO overtimeApprovalVO;
//    private OvertimeApproval overtimeApproval;
//    private OvertimeApprovalDTO overtimeApprovalDTO;
//
//    private RWApprovalVO rwApprovalVO;
//    private RWApproval rwApproval;
//    private RWApprovalDTO rwApprovalDTO;
//
//    private VacationApprovalVO vacationApprovalVO;
//    private VacationApproval vacationApproval;
//    private VacationApprovalDTO vacationApprovalDTO;
//
//
//    @BeforeEach
//    void setUp() {
//        btApprovalVO = new BTApprovalVO();
//        btApprovalVO.setStartTime("2024-06-11 09:00:00");
//        btApprovalVO.setEndTime("2024-06-11 17:00:00");
//        btApprovalVO.setDestination("대전");
//        btApprovalVO.setContent("프로젝트 관련 미팅 및 보고");
//        btApprovalVO.setRequesterId(1);
//
//        btApproval = BTApproval.builder()
//                .startTime(btApprovalVO.getStartTime())
//                .endTime(btApprovalVO.getEndTime())
//                .destination(btApprovalVO.getDestination())
//                .content(btApprovalVO.getContent())
//                .writeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .requesterId(btApprovalVO.getRequesterId())
//                .build();
//
//        overtimeApprovalVO = new OvertimeApprovalVO();
//        overtimeApprovalVO.setStartTime("2024-06-11 19:00:00");
//        overtimeApprovalVO.setEndTime("2024-06-11 20:00:00");
//        overtimeApprovalVO.setContent("서버 업그레이드 및 테스트");
//        overtimeApprovalVO.setRequesterId(1);
//
//        overtimeApproval = OvertimeApproval.builder()
//                .startTime(overtimeApprovalVO.getStartTime())
//                .endTime(overtimeApprovalVO.getEndTime())
//                .content(overtimeApprovalVO.getContent())
//                .writeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .requesterId(overtimeApprovalVO.getRequesterId())
//                .build();
//
//
//        rwApprovalVO = new RWApprovalVO();
//        rwApprovalVO.setStartTime("2024-06-11 00:00:00");
//        rwApprovalVO.setEndTime("2025-06-11 00:00:00");
//        rwApprovalVO.setContent("건강 관리 및 출산 준비");
//        rwApprovalVO.setRequesterId(1);
//
//        rwApproval = RWApproval.builder()
//                .startTime(rwApprovalVO.getStartTime())
//                .endTime(rwApprovalVO.getEndTime())
//                .content(rwApprovalVO.getContent())
//                .writeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .requesterId(rwApprovalVO.getRequesterId())
//                .build();
//
//        vacationApprovalVO = new VacationApprovalVO();
//        vacationApprovalVO.setStartTime("2024-06-11 00:00:00");
//        vacationApprovalVO.setEndTime("2024-06-13 00:00:00");
//        vacationApprovalVO.setContent("연차 사용");
//        vacationApprovalVO.setInfoId(1);
//        vacationApprovalVO.setRequesterId(1);
//
//        vacationApproval = VacationApproval.builder()
//                .startTime(vacationApprovalVO.getStartTime())
//                .endTime(vacationApprovalVO.getEndTime())
//                .content(vacationApprovalVO.getContent())
//                .infoId(vacationApprovalVO.getInfoId())
//                .writeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .requesterId(vacationApprovalVO.getRequesterId())
//                .build();
//
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//    }
//
//    @Test
//    @Transactional
//    void addBTApproval() {
//
//        when(btApprovalRepository.save(any(BTApproval.class))).thenReturn(btApproval);
//        when(mapper.map(btApproval, BTApprovalDTO.class)).thenReturn(btApprovalDTO);
//
//        BTApprovalDTO result = approvalService.addBTApproval(btApprovalVO);
//
//        assertThat(result).isEqualTo(btApprovalDTO);
//    }
//
//    @Test
//    void addOvertimeApproval() {
//
//        when(oApprovalRepository.save(any(OvertimeApproval.class))).thenReturn(overtimeApproval);
//        when(mapper.map(overtimeApproval, OvertimeApprovalDTO.class)).thenReturn(overtimeApprovalDTO);
//
//        OvertimeApprovalDTO result = approvalService.addOvertimeApproval(overtimeApprovalVO);
//
//        assertThat(result).isEqualTo(overtimeApprovalDTO);
//    }
//
//    @Test
//    void addRWApproval() {
//
//        when(rwApprovalRepository.save(any(RWApproval.class))).thenReturn(rwApproval);
//        when(mapper.map(rwApproval, RWApprovalDTO.class)).thenReturn(rwApprovalDTO);
//
//        RWApprovalDTO result = approvalService.addRWApproval(rwApprovalVO);
//
//        assertThat(result).isEqualTo(rwApprovalDTO);
//    }
//
//    @Test
//    void addVacationApproval() {
//
//        when(vApprovalRepository.save(any(VacationApproval.class))).thenReturn(vacationApproval);
//        when(mapper.map(vacationApproval, VacationApprovalDTO.class)).thenReturn(vacationApprovalDTO);
//
//        VacationApprovalDTO result = approvalService.addVacationApproval(vacationApprovalVO);
//
//        assertThat(result).isEqualTo(vacationApprovalDTO);
//    }
//
//    @Test
//    void cancelBTApproval() {
//
//        int btApprovalId = 1;
//
//        when(btApprovalRepository.findById(btApprovalId)).thenReturn(Optional.of(btApproval));
//        when(mapper.map(btApproval, BTApprovalDTO.class)).thenReturn(btApprovalDTO);
//
//        System.out.println(btApproval);
//        System.out.println(btApprovalDTO);
//
//        BTApprovalDTO result = approvalService.cancelBTApproval(btApprovalId);
//        System.out.println(result);
////
////        verify(btApproval).setCancelYN(YN.Y);
////        verify(btApprovalRepository).save(btApproval);
////        assertNull(result);
//    }
//
//    @Test
//    void cancelOvertimeApproval() {
//
//
//    }
//
//    @Test
//    void cancelRWApproval() {
//    }
//
//    @Test
//    void cancelVacationApproval() {
//    }
//
//    @Test
//    void viewBTApproval() {
//    }
//
//    @Test
//    void viewOvertimeApproval() {
//    }
//
//    @Test
//    void viewRWApproval() {
//    }
//
//    @Test
//    void viewVacationApproval() {
//    }
//}
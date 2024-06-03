//package org.highfives.grid.vacation.command.service;
//
//import jakarta.transaction.Transactional;
//import org.assertj.core.api.Assertions;
//import org.highfives.grid.user.command.aggregate.*;
//import org.highfives.grid.user.command.repository.UserRepository;
//import org.highfives.grid.user.query.service.UserService;
//import org.highfives.grid.vacation.command.entity.VacationPolicy;
//import org.highfives.grid.vacation.command.entity.VacationType;
//import org.highfives.grid.vacation.command.repository.VacationInfoRepository;
//import org.highfives.grid.vacation.command.repository.VacationPolicyRepository;
//import org.highfives.grid.vacation.command.repository.VacationTypeRepository;
//import org.highfives.grid.vacation.command.vo.GiveVacation;
//import org.highfives.grid.vacation.command.vo.ModifyPolicy;
//import org.highfives.grid.vacation.command.vo.RegistPolicy;
//import org.highfives.grid.vacation.command.vo.RegistVacationType;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//class VacationServiceTest {
//
//    private VacationPolicyRepository vacationPolicyRepository;
//    private VacationInfoRepository vacationInfoRepository;
//    private VacationTypeRepository vacationTypeRepository;
//    private VacationService vacationService;
//    private UserRepository userRepository;
//    private UserService userService;
//
//    @Autowired
//    public VacationServiceTest(VacationPolicyRepository vacationPolicyRepository, VacationInfoRepository vacationInfoRepository, VacationTypeRepository vacationTypeRepository, VacationService vacationService, UserRepository userRepository, UserService userService) {
//        this.vacationPolicyRepository = vacationPolicyRepository;
//        this.vacationInfoRepository = vacationInfoRepository;
//        this.vacationTypeRepository = vacationTypeRepository;
//        this.vacationService = vacationService;
//        this.userRepository = userRepository;
//        this.userService = userService;
//    }
//
//    @Test
//    @DisplayName("휴가 정책 수정 테스트")
//    @Transactional
//    void modifyVacationPolicy() {
//        //given
//        ModifyPolicy modifyContent = new ModifyPolicy();
//        modifyContent.setContent("수정이 됐나요?");
//        int id = 1;
//
//        //when
//        vacationService.modifyVacationPolicy(modifyContent, id);
//
//        //then
//        Assertions.assertThat(modifyContent.getContent()).isEqualTo(vacationPolicyRepository.findById(1).get().getContent());
//
//    }
//
//    @Test
//    @DisplayName("휴가 정책 삭제 테스트")
//    @Transactional
//    void deleteVacationPolicy() {
//        //given
//        int id = 1;
//
//        //when
//        vacationService.deleteVacationPolicy(id);
//
//        //then
//        Assertions.assertThat(vacationPolicyRepository.findById(id)).isEmpty();
//    }
//
//    @Test
//    @DisplayName("휴가 정책 등록 테스트")
//    @Transactional
//    void registVacationPolicy() {
//        //given
//        RegistPolicy policyInfo = new RegistPolicy();
//        policyInfo.setContent("휴가 정책 등록 test");
//        policyInfo.setTypeId(1);
//
//        //when
//        vacationService.registVacationPolicy(policyInfo);
//
//        //then
//        List<VacationPolicy> vacationPolicies = vacationPolicyRepository.findAll();
//        Assertions.assertThat(policyInfo.getContent()).isEqualTo(vacationPolicies.get(vacationPolicies.size() - 1).getContent());
//
//    }
//
//    @Test
//    @DisplayName("월차 제공 테스트")
//    @Transactional
//    void giveAnnualVacationBeforeYear() {
//        //given
//        Employee employee = Employee.builder()
//                .email("qwer")
//                .pwd("1234")
//                .employeeName("길동이")
//                .employeeNumber("11483")
//                .gender(Gender.M)
//                .phoneNumber("010-6556-8888")
//                .callNumber("010-1234-1111")
//                .zipCode("12345")
//                .address("test")
//                .assignedTask("test")
//                .joinTime("2023-12-31")
//                .joinType(JoinType.NEW)
//                .resignTime("test")
//                .resignYn(YN.N)
//                .workType(WorkType.R)
//                .contractStartTime("test")
//                .contractEndTime("test")
//                .salary(5200)
//                .absenceYn(YN.Y)
//                .dutiesId(1)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .build();
//        userRepository.save(employee);
//        int size = userService.findAllUsers().size();
//        int id = userService.findAllUsers().get(size - 1).getId();
//
//        //when
//        vacationService.giveAnnualVacationBeforeYear();
//        System.out.println(vacationInfoRepository.findAll().toString());
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(id, 2).getVacationNum()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("연차 제공 테스트")
//    @Transactional
//    void giveAnnualVacationAfterYear() {
//        //given
//        Employee employee = Employee.builder()
//                .email("qwer")
//                .pwd("1234")
//                .employeeName("길동이")
//                .employeeNumber("11483")
//                .gender(Gender.M)
//                .phoneNumber("010-6556-8888")
//                .callNumber("010-1234-1111")
//                .zipCode("12345")
//                .address("test")
//                .assignedTask("test")
//                .joinTime("2023-12-31")
//                .joinType(JoinType.NEW)
//                .resignTime("test")
//                .resignYn(YN.N)
//                .workType(WorkType.R)
//                .contractStartTime("test")
//                .contractEndTime("test")
//                .salary(5200)
//                .absenceYn(YN.Y)
//                .dutiesId(1)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .build();
//        userRepository.save(employee);
//        int size = userService.findAllUsers().size();
//        int id = userService.findAllUsers().get(size - 1).getId();
//
//        //when
//        vacationService.giveAnnualVacationAfterYear();
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(id, 1).getVacationNum()).isEqualTo(16);
//    }
//
//    @Test
//    @DisplayName("정기휴가 제공 테스트")
//    @Transactional
//    void giveRegularVacation() {
//        //given
//        Employee employee = Employee.builder()
//                .email("qwer")
//                .pwd("1234")
//                .employeeName("길동이")
//                .employeeNumber("11483")
//                .gender(Gender.M)
//                .phoneNumber("010-6556-8888")
//                .callNumber("010-1234-1111")
//                .zipCode("12345")
//                .address("test")
//                .assignedTask("test")
//                .joinTime("2023-12-31")
//                .joinType(JoinType.NEW)
//                .resignTime("test")
//                .resignYn(YN.N)
//                .workType(WorkType.R)
//                .contractStartTime("test")
//                .contractEndTime("test")
//                .salary(5200)
//                .absenceYn(YN.Y)
//                .dutiesId(1)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .build();
//        userRepository.save(employee);
//        int size = userService.findAllUsers().size();
//        int id = userService.findAllUsers().get(size - 1).getId();
//
//        //when
//        vacationService.giveRegularVacation();
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(id, 4).getVacationNum()).isEqualTo(4);
//    }
//
//    @Test
//    @DisplayName("보건휴가 제공 테스트")
//    @Transactional
//    void giveHealthVacation() {
//        //given
//        Employee employee = Employee.builder()
//                .email("qwer")
//                .pwd("1234")
//                .employeeName("길동이")
//                .employeeNumber("11483")
//                .gender(Gender.M)
//                .phoneNumber("010-6556-8888")
//                .callNumber("010-1234-1111")
//                .zipCode("12345")
//                .address("test")
//                .assignedTask("test")
//                .joinTime("2023-12-31")
//                .joinType(JoinType.NEW)
//                .resignTime("test")
//                .resignYn(YN.N)
//                .workType(WorkType.R)
//                .contractStartTime("test")
//                .contractEndTime("test")
//                .salary(5200)
//                .absenceYn(YN.Y)
//                .dutiesId(1)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .build();
//        userRepository.save(employee);
//        int size = userService.findAllUsers().size();
//        int id = userService.findAllUsers().get(size - 1).getId();
//
//        //when
//        vacationService.giveHealthVacation();
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(id, 3).getVacationNum()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("관리자 직접 휴가 제공 테스트")
//    @Transactional
//    void giveVacationByManager() {
//        //given
//        GiveVacation giveVacation = new GiveVacation();
//        giveVacation.setVacationNum(100);
//        giveVacation.setEndTime("2024-12-31");
//        giveVacation.setEmployeeId(1);
//        giveVacation.setTypeId(2);
//
//        //when
//        vacationService.giveVacationByManager(giveVacation);
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(1, 2).getVacationNum()).isEqualTo(100);
//
//    }
//
//    @Test
//    @DisplayName("휴가 종류 등록 테스트")
//    @Transactional
//    void registVacationType() {
//        //given
//        RegistVacationType typeInfo = new RegistVacationType();
//        typeInfo.setTypeName("test");
//        int max = 0;
//
//        //when
//        vacationService.registVacationType(typeInfo);
//
//        List<VacationType> types = vacationTypeRepository.findAll();
//        for (int i = 0; i < types.size(); i++) {
//            if (max < types.get(i).getId()) {
//                max = types.get(i).getId();
//            }
//        }
//
//        //then
//        Assertions.assertThat(vacationTypeRepository.findById(max).get().getTypeName()).isEqualTo("test");
//    }
//
//    @Test
//    @DisplayName("휴가개수 증가 테스트")
//    @Transactional
//    void plusVacationNum() {
//        //given
//        int typeId = 5;
//        int employeeId = 1;
//
//        //when
//        vacationService.plusVacationNum(1, 5);
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(1, 1).getVacationNum()).isEqualTo(15.5);
//    }
//
//    @Test
//    @DisplayName("휴가개수 감소 테스트")
//    @Transactional
//    void minusVacationNum() {
//        //given
//        int typeId = 5;
//        int employeeId = 1;
//
//        //when
//        vacationService.minusVacationNum(1, 5);
//
//        //then
//        Assertions.assertThat(vacationInfoRepository.findByEmployeeIdAndTypeId(1, 1).getVacationNum()).isEqualTo(14.5);
//    }
//}

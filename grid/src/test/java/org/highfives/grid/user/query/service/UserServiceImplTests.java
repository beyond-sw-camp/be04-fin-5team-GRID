//package org.highfives.grid.user.query.service;
//
//import org.highfives.grid.approval.query.dto.EmpStatusDTO;
//import org.highfives.grid.user.command.aggregate.Gender;
//import org.highfives.grid.user.command.aggregate.JoinType;
//import org.highfives.grid.user.command.aggregate.WorkType;
//import org.highfives.grid.user.command.aggregate.YN;
//import org.highfives.grid.user.exception.InvalidNumberException;
//import org.highfives.grid.user.exception.UserNotFoundException;
//import org.highfives.grid.user.query.dto.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Stream;
//@SpringBootTest
//class UserServiceImplTests {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    static Stream<Arguments> findAllUsersInfos() {
//
//        EmpStatusDTO empStatusDTO = new EmpStatusDTO(2, "출장", "2024-06-11", "2024-06-12");
//        Map<Integer, EmpStatusDTO> example = new HashMap<>();
//        example.put(2, empStatusDTO);
//
//        return Stream.of(
//                // user 서비스 정상 동작 시에 예외가 발생하지 않는 것을 확인하기 위함이므로, 타 서비스의 함수는 가져오지 않는다.
//                Arguments.of(PageRequest.of(0, 12), example, "ROLE_ADMIN"),
//                Arguments.of(PageRequest.of(0, 12), example, "ROLE_USER"),
//                Arguments.of(PageRequest.of(0, 12), null, "ROLE_ADMIN"),
//                Arguments.of(PageRequest.of(0, 12), null, "ROLE_USER"),
//                Arguments.of(PageRequest.of(4, 12), null, "ROLE_ADMIN"),
//                Arguments.of(PageRequest.of(4, 12), null, "ROLE_USER"),
//                Arguments.of(PageRequest.of(100, 12), null, "ROLE_ADMIN"),
//                Arguments.of(PageRequest.of(100, 12), null, "ROLE_USER")
//        );
//    }
//
//    static Stream<Arguments> findUsersByNameInfos() {
//
//        EmpStatusDTO empStatusDTO = new EmpStatusDTO(2, "출장", "2024-06-11", "2024-06-12");
//        Map<Integer, EmpStatusDTO> example = new HashMap<>();
//        example.put(2, empStatusDTO);
//
//        return Stream.of(
//                Arguments.of("정",PageRequest.of(0, 12), example, "ROLE_ADMIN"),
//                Arguments.of("정",PageRequest.of(0, 12), example, "ROLE_USER"),
//                Arguments.of("정",PageRequest.of(0, 12), null, "ROLE_ADMIN"),
//                Arguments.of("정",PageRequest.of(0, 12), null, "ROLE_USER"),
//                Arguments.of("정",PageRequest.of(4, 12), example, "ROLE_ADMIN"),
//                Arguments.of("정",PageRequest.of(4, 12), example, "ROLE_USER"),
//                Arguments.of("정",PageRequest.of(4, 12), null, "ROLE_ADMIN"),
//                Arguments.of("정",PageRequest.of(4, 12), null, "ROLE_USER"),
//                Arguments.of("정",PageRequest.of(100, 12), example, "ROLE_ADMIN"),
//                Arguments.of("정",PageRequest.of(100, 12), example, "ROLE_USER"),
//                Arguments.of("정",PageRequest.of(100, 12), null, "ROLE_ADMIN"),
//                Arguments.of("정",PageRequest.of(100, 12), null, "ROLE_USER"),
//                // 없는 이름이라도 예외가 안나고 반환이 안되는 것을 확인하기
//                Arguments.of("뚫",PageRequest.of(0, 12), example, "ROLE_ADMIN"),
//                Arguments.of("뚫",PageRequest.of(0, 12), example, "ROLE_USER"),
//                Arguments.of("뚫",PageRequest.of(0, 12), null, "ROLE_ADMIN"),
//                Arguments.of("뚫",PageRequest.of(0, 12), null, "ROLE_USER"),
//                Arguments.of("뚫",PageRequest.of(4, 12), example, "ROLE_ADMIN"),
//                Arguments.of("뚫",PageRequest.of(4, 12), example, "ROLE_USER"),
//                Arguments.of("뚫",PageRequest.of(4, 12), null, "ROLE_ADMIN"),
//                Arguments.of("뚫",PageRequest.of(4, 12), null, "ROLE_USER"),
//                Arguments.of("뚫",PageRequest.of(100, 12), example, "ROLE_ADMIN"),
//                Arguments.of("뚫",PageRequest.of(100, 12), example, "ROLE_USER"),
//                Arguments.of("뚫",PageRequest.of(100, 12), null, "ROLE_ADMIN"),
//                Arguments.of("뚫",PageRequest.of(100, 12), null, "ROLE_USER")
//        );
//    }
//
//    static Stream<Arguments> findUserByEmployeeNumInfo() {
//
//        EmpStatusDTO empStatusDTO = new EmpStatusDTO(2, "출장", "2024-06-11", "2024-06-12");
//        Map<Integer, EmpStatusDTO> example = new HashMap<>();
//        example.put(2, empStatusDTO);
//
//        return Stream.of(
//                Arguments.of("9999999", example),
//                Arguments.of("9999999", null),
//                Arguments.of("2401020", example),
//                Arguments.of("2401020", null)
//        );
//    }
//
//    static Stream<Arguments> checkNameByEmailInfo() {
//
//        return Stream.of(
//                Arguments.of("gridpeople2024@gmail.com"),
//                Arguments.of("first@naver.com")
//        );
//    }
//
//    //전체 유저 조회
//    @DisplayName("전체 유저 리스트 조회")
//    @Test
//    void findListTest() {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findList()
//        );
//    }
//
//    @DisplayName("전체 유저 리스트 조회(페이징)")
//    @ParameterizedTest
//    @MethodSource("findAllUsersInfos")
//    void findAllUsersTest(Pageable pageable, Map<Integer, EmpStatusDTO> absenceInfo, String auth) {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findAllUsers(pageable, absenceInfo, auth)
//        );
//    }
//
//    @DisplayName("전체 유저 리스트 조회(페이징, 이름 검색)")
//    @ParameterizedTest
//    @MethodSource("findUsersByNameInfos")
//    void findUsersByNameTest(String name, Pageable pageable, Map<Integer, EmpStatusDTO> ndAbsenceInfo, String auth) {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findUsersByName(name, pageable, ndAbsenceInfo, auth)
//        );
//    }
//
//    @DisplayName("잘못된 Page 입력 시 예외 발생")
//    @Test
//    void invalidPageIndexTest() {
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            PageRequest.of(-1, 12);
//        });
//    }
//
//    //리더(팀장, 부서장) 정보 조회
//    @DisplayName("팀장, 부서장 정보 조회")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 2, 56})
//    void findLeaderInfoTest(int id) {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findLeaderInfo(id)
//        );
//    }
//
//    @DisplayName("등록되지 않은 id 입력 시 팀장, 부서장 정보 조회")
//    @ParameterizedTest
//    @ValueSource(ints = {-1, 0, 54321})
//    void findLeaderInfoInvalidTest(int id) {
//        Assertions.assertThrows(NullPointerException.class,
//                () -> userService.findLeaderInfo(id)
//        );
//    }
//
//    //사번으로 사원 정보 조회
//    @DisplayName("유저 정보 조회(사원번호) 테스트 - 정상 동작")
//    @ParameterizedTest
//    @MethodSource("findUserByEmployeeNumInfo")
//    void findUserByEmployeeNumNormalTest1(String eNum, Map<Integer, EmpStatusDTO> ndAbsenceInfo) {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findUserByEmployeeNum(eNum, ndAbsenceInfo)
//        );
//    }
//
//    @DisplayName("유저 정보 조회(사원번호) 테스트 - 예상 결과 도출")
//    @Test
//    void findUserByEmployeeNumNormalTest2() {
//
//        UserDTO actual = userService.findUserByEmployeeNum("9999999", null);
//        actual.setProfilePath(null);
//        actual.setSealPath(null);
//        UserDTO userDTO = UserDTO.builder()
//                .id(1)
//                .absenceYn(YN.N)
//                .contractStartTime("1999-12-31")
//                .department(new DepartmentDTO(1, "IT"))
//                .duties(new DutiesDTO(4, "팀원"))
//                .email("gridpeople2024@gmail.com")
//                .employeeNumber("9999999")
//                .gender(Gender.M)
//                .salary(9999)
//                .joinTime("1999-12-31")
//                .joinType(JoinType.NEW)
//                .name("Admin")
//                .assignedTask("관리자 계정")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("02-1234-5678")
//                .position(new PositionDTO(1, "사원"))
//                .resignYn(YN.N)
//                .zipCode(0)
//                .address(" ")
//                .team(new TeamDTO(1, "시스템 개발팀"))
//                .workType(WorkType.R)
//                .build();
//
//        Assertions.assertEquals(
//                userDTO, actual);
//    }
//
//    @DisplayName("사원 번호 조회 예외 테스트 (잘못된 사원 번호 양식)")
//    @ParameterizedTest
//    @ValueSource(strings = {"-1", "0", "240101"})
//    void findUserByEmployeeNumInvalidTest1(String eNum) {
//
//        Assertions.assertThrows(InvalidNumberException.class,
//                () -> userService.findUserByEmployeeNum(eNum, null)
//        );
//    }
//
//    @DisplayName("사원 번호 조회 예외 테스트 (등록되지 않은 사원 번호)")
//    @Test
//    void findUserByEmployeeNumInvalidTest2() {
//
//        Assertions.assertThrows(UserNotFoundException.class,
//                () -> userService.findUserByEmployeeNum("2401999", null)
//        );
//    }
//
//    // id로 사원 정보 조회
//    @DisplayName("유저 정보 조회(유저 id) 테스트 - 정상 동작")
//    @Test
//    void findUserByIdNormalTest1() {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findUserById(1)
//        );
//    }
//
//    @DisplayName("유저 정보 조회(유저 id) 테스트 - 예상 결과 도출")
//    @Test
//    void findUserByIdNormalTest2() {
//
//        UserDTO actual = userService.findUserById(1);
//        actual.setProfilePath(null);
//        actual.setSealPath(null);
//        UserDTO userDTO = UserDTO.builder()
//                .id(1)
//                .absenceYn(YN.N)
//                .contractStartTime("1999-12-31")
//                .department(new DepartmentDTO(1, "IT"))
//                .duties(new DutiesDTO(4, "팀원"))
//                .email("gridpeople2024@gmail.com")
//                .employeeNumber("9999999")
//                .gender(Gender.M)
//                .salary(9999)
//                .joinTime("1999-12-31")
//                .joinType(JoinType.NEW)
//                .name("Admin")
//                .assignedTask("관리자 계정")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("02-1234-5678")
//                .position(new PositionDTO(1, "사원"))
//                .resignYn(YN.N)
//                .zipCode(0)
//                .address(" ")
//                .team(new TeamDTO(1, "시스템 개발팀"))
//                .workType(WorkType.R)
//                .build();
//
//        Assertions.assertEquals(
//                userDTO, actual);
//    }
//
//    @DisplayName("유저 id 조회 예외 테스트 (잘못된 id 양식)")
//    @ParameterizedTest
//    @ValueSource(ints = {-1, 0})
//    void findUserByIdInvalidTest1(int eNum) {
//
//        Assertions.assertThrows(InvalidNumberException.class,
//                () -> userService.findUserById(eNum)
//        );
//    }
//
//    @DisplayName("유저 id 조회 예외 테스트 (등록되지 않은 id 번호)")
//    @Test
//    void findUserByIdInvalidTest2() {
//
//        Assertions.assertThrows(UserNotFoundException.class,
//                () -> userService.findUserById(54321)
//        );
//    }
//
//    //email 로 사원 정보 조회
//    @DisplayName("유저 정보 조회(이메일) 테스트 - 정상 동작")
//    @Test
//    void findUserByEmailNormalTest1() {
//        Assertions.assertDoesNotThrow(
//                () -> userService.findUserByEmail("gridpeople2024@gmail.com")
//        );
//    }
//
//    @DisplayName("유저 정보 조회(이메일) 테스트 - 예상 결과 도출")
//    @Test
//    void findUserByEmailNormalTest2() {
//
//        UserDTO actual = userService.findUserByEmail("gridpeople2024@gmail.com");
//        actual.setProfilePath(null);
//        actual.setSealPath(null);
//        UserDTO userDTO = UserDTO.builder()
//                .id(1)
//                .absenceYn(YN.N)
//                .contractStartTime("1999-12-31")
//                .department(new DepartmentDTO(1, "IT"))
//                .duties(new DutiesDTO(4, "팀원"))
//                .email("gridpeople2024@gmail.com")
//                .employeeNumber("9999999")
//                .gender(Gender.M)
//                .salary(9999)
//                .joinTime("1999-12-31")
//                .joinType(JoinType.NEW)
//                .name("Admin")
//                .assignedTask("관리자 계정")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("02-1234-5678")
//                .position(new PositionDTO(1, "사원"))
//                .resignYn(YN.N)
//                .zipCode(0)
//                .address(" ")
//                .team(new TeamDTO(1, "시스템 개발팀"))
//                .workType(WorkType.R)
//                .build();
//
//        Assertions.assertEquals(
//                userDTO, actual);
//    }
//
//    @DisplayName("이메일 조회 예외 테스트 (등록되지 않은 이메일)")
//    @Test
//    void findUserByEmailInvalidTest2() {
//
//        Assertions.assertThrows(UserNotFoundException.class,
//                () -> userService.findUserByEmail("exceptionTEST@gmail.com")
//        );
//    }
//
//    // 직책 / 직위 정보 조회
//    @DisplayName("직위 정보 조회 테스트")
//    @Test
//    void findPositionsTest() {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.findPositions()
//        );
//    }
//
//    @DisplayName("직책 정보 조회 테스트")
//    @Test
//    void findDutiesTest() {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.findDuties()
//        );
//    }
//
//    // 이메일로 사원명 조회
//    @DisplayName("등록 정보 일치(이메일, 사원명) 조회 테스트")
//    @ParameterizedTest
//    @MethodSource("checkNameByEmailInfo")
//    void checkNameByEmailNormalTest(String email) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.checkNameByEmail(email)
//        );
//    }
//
//    @DisplayName("등록 정보 일치(이메일, 사원명) 조회 테스트 - 미등록된 이메일")
//    @Test
//    void checkNameByEmailInvalidTest() {
//
//        Assertions.assertThrows(UserNotFoundException.class,
//                () -> userService.checkNameByEmail("testemailcheck@gmail.com")
//        );
//    }
//
//    //팀에 소속된 직원 정보 조회
//    @DisplayName("팀 소속 직원 조회 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 2})
//    void findTeamListNormalTest(int id) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.findTeamList(id)
//        );
//    }
//
//    @DisplayName("팀 소속 직원 조회 예외 테스트(잘못된 팀 id)")
//    @ParameterizedTest
//    @ValueSource(ints = {-1, 0})
//    void findTeamListInvalidTest1(int id) {
//
//        Assertions.assertThrows(InvalidNumberException.class,
//                () -> userService.findTeamList(id)
//        );
//    }
//
//    @DisplayName("팀 소속 직원 조회 예외 테스트(잘못된 팀 id)")
//    @ParameterizedTest
//    @ValueSource(ints = {99, 9999})
//    void findTeamListInvalidTest2(int id) {
//
//        Assertions.assertThrows(UserNotFoundException.class,
//                () -> userService.findTeamList(id)
//        );
//    }
//
//}
//package org.highfives.grid.user.command.service;
//
//import jakarta.transaction.Transactional;
//import org.highfives.grid.user.command.aggregate.Gender;
//import org.highfives.grid.user.command.aggregate.JoinType;
//import org.highfives.grid.user.command.aggregate.Role;
//import org.highfives.grid.user.command.aggregate.WorkType;
//import org.highfives.grid.user.command.dto.UserDTO;
//import org.highfives.grid.user.exception.InvalidInfoException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Stream;
//
//import static org.highfives.grid.user.command.aggregate.Gender.M;
//import static org.highfives.grid.user.command.aggregate.JoinType.NEW;
//import static org.highfives.grid.user.command.aggregate.Role.ROLE_ADMIN;
//import static org.highfives.grid.user.command.aggregate.Role.ROLE_USER;
//import static org.highfives.grid.user.command.aggregate.WorkType.C;
//import static org.highfives.grid.user.command.aggregate.WorkType.R;
//import static org.highfives.grid.user.command.aggregate.YN.N;
//
//@SpringBootTest
//class UserServiceImplTests {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    static Stream<Arguments> userList() {
//        List<UserDTO> userDTO = new ArrayList<>();
//
//        userDTO.add(new UserDTO("testemail@gamil.com","testpwd","tester1",
//                "2499999", M,"010-1234-5678",
//                "2024-05-06", NEW, WorkType.R, "2024-05-06",ROLE_USER,
//                4,1,1,1));
//
//        userDTO.add(new UserDTO("testemail2@gamil.com","testpwd","tester1",
//                "2399999", M,"010-5678-1234",
//                "2024-05-06", NEW, WorkType.R, "2024-05-06",ROLE_USER,
//                4,1,1,1));
//
//        return Stream.of(
//                Arguments.of(userDTO)
//        );
//    }
//
//    static Stream<Arguments> modifyInfo() {
//
//        UserDTO userDTO = UserDTO.builder()
//                .id(71)
//                .email("test19990101@gmail.com")
//                .name("Admin3")
//                .employeeNumber("9999998")
//                .gender(M)
//                .pwd("")
//                .contractStartTime("2009-12-31")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("010-9999-2222")
//                .joinTime("1999-12-31")
//                .joinType(NEW)
//                .workType(R)
//                .role(ROLE_ADMIN)
//                .dutiesId(4)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .zipCode("0")
//                .address("")
//                .build();
//
//        UserDTO userDTO2 = UserDTO.builder()
//                .id(1)
//                .email("gridpeople2024@gmail.com")
//                .name("Admin")
//                .employeeNumber("9999999")
//                .gender(M)
//                .pwd("")
//                .contractStartTime("2009-12-31")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("010-9999-2222")
//                .joinTime("1999-12-31")
//                .joinType(NEW)
//                .workType(R)
//                .role(ROLE_ADMIN)
//                .dutiesId(1)
//                .positionId(4)
//                .teamId(4)
//                .departmentId(4)
//                .zipCode("0")
//                .address("")
//                .build();
//
//        return Stream.of(
//                Arguments.of(71, userDTO),
//                Arguments.of(1, userDTO2)
//        );
//    }
//
//    static Stream<Arguments> modifyMultiUsersInfo() {
//
//        List<UserDTO> userDTOList = new ArrayList<>();
//
//        UserDTO userDTO = UserDTO.builder()
//                .id(71)
//                .email("test19990101@gmail.com")
//                .name("Admin3")
//                .employeeNumber("9999998")
//                .gender(M)
//                .pwd("")
//                .contractStartTime("2009-12-31")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("010-9999-2222")
//                .joinTime("1999-12-31")
//                .joinType(NEW)
//                .workType(R)
//                .role(ROLE_ADMIN)
//                .dutiesId(4)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .zipCode("0")
//                .address("")
//                .build();
//
//        UserDTO userDTO2 = UserDTO.builder()
//                .id(1)
//                .email("gridpeople2024@gmail.com")
//                .name("Admin")
//                .employeeNumber("9999999")
//                .gender(M)
//                .pwd("")
//                .contractStartTime("2009-12-31")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("010-9999-2222")
//                .joinTime("1999-12-31")
//                .joinType(NEW)
//                .workType(R)
//                .role(ROLE_ADMIN)
//                .dutiesId(1)
//                .positionId(4)
//                .teamId(4)
//                .departmentId(4)
//                .zipCode("0")
//                .address("")
//                .build();
//
//        userDTOList.add(userDTO2);
//        userDTOList.add(userDTO);
//
//        return Stream.of(
//                Arguments.of(userDTOList)
//        );
//    }
//
//    static Stream<Arguments> modifyMultiUsersInvalidInfo() {
//
//        List<UserDTO> userDTOList = new ArrayList<>();
//
//        UserDTO userDTO = UserDTO.builder()
//                .id(71)
//                .email("test19990101@gmail.com")
//                .name("Admin2")
//                .employeeNumber("9999998")
//                .gender(M)
//                .pwd("")
//                .contractStartTime("2009-12-31")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("010-9999-2222")
//                .joinTime("1999-12-31")
//                .joinType(NEW)
//                .workType(R)
//                .role(ROLE_ADMIN)
//                .dutiesId(4)
//                .positionId(1)
//                .teamId(1)
//                .departmentId(1)
//                .zipCode("0")
//                .address("")
//                .build();
//
//        UserDTO userDTO2 = UserDTO.builder()
//                .id(1)
//                .email("gridpeople2023@gmail.com")
//                .name("Admin")
//                .employeeNumber("9999999")
//                .gender(M)
//                .pwd("")
//                .contractStartTime("2009-12-31")
//                .contractEndTime("2999-12-31")
//                .phoneNumber("010-9999-2222")
//                .joinTime("1999-12-31")
//                .joinType(NEW)
//                .workType(R)
//                .role(ROLE_ADMIN)
//                .dutiesId(1)
//                .positionId(4)
//                .teamId(4)
//                .departmentId(4)
//                .zipCode("0")
//                .address("")
//                .build();
//
//        userDTOList.add(userDTO2);
//        userDTOList.add(userDTO);
//
//        return Stream.of(
//                Arguments.of(userDTOList)
//        );
//    }
//
//    static Stream<Arguments> duplicateInfoCheckNormalData() {
//
//        UserDTO userDTO = UserDTO.builder()
//                .email("test19990102@gmail.com")
//                .employeeNumber("9999997")
//                .phoneNumber("010-9999-2222")
//                .build();
//
//        return Stream.of(
//                Arguments.of(userDTO)
//        );
//    }
//
//    static Stream<Arguments> duplicateInfoCheckDuplicateData() {
//
//        UserDTO userDTO = UserDTO.builder()
//                .email("test19990101@gmail.com")
//                .employeeNumber("9999997")
//                .phoneNumber("010-9999-2222")
//                .build();
//
//        UserDTO userDTO2 = UserDTO.builder()
//                .email("test19990102@gmail.com")
//                .employeeNumber("9999998")
//                .phoneNumber("010-9999-2222")
//                .build();
//
//        UserDTO userDTO3 = UserDTO.builder()
//                .email("test19990102@gmail.com")
//                .employeeNumber("9999997")
//                .phoneNumber("010-9999-1111")
//                .build();
//
//        return Stream.of(
//                Arguments.of(userDTO),
//                Arguments.of(userDTO2),
//                Arguments.of(userDTO3)
//        );
//    }
//
//    @DisplayName("다중 유저 등록 테스트 - 정상 동작")
//    @ParameterizedTest
//    @MethodSource("userList")
//    @Transactional
//    void addMultiUserNormalTest(List<UserDTO> givenInfo) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.addMultiUser(givenInfo)
//        );
//    }
//
//    //잘못된 값에 대한 예외 처리는 다른 메소드에서 하고 있으므로 다른데서 테스트 한다.
//    @DisplayName("단일 유저 수정 테스트 - 정상 동작")
//    @ParameterizedTest
//    @MethodSource("modifyInfo")
//    @Transactional
//    void modifyUserNormalTest(int id, UserDTO infos) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.modifyUser(id, infos)
//        );
//    }
//
//    @DisplayName("다중 유저 수정 테스트 - 정상 동작")
//    @ParameterizedTest
//    @MethodSource("modifyMultiUsersInfo")
//    @Transactional
//    void modifyMultiUserNormalTest(List<UserDTO> userList) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.modifyMultiUser(userList)
//        );
//    }
//    @DisplayName("다중 유저 수정 예외 테스트 - 일치하지 않는 이름과 이메일")
//    @ParameterizedTest
//    @MethodSource("modifyMultiUsersInvalidInfo")
//    @Transactional
//    void modifyMultiUserInvalidTest1(List<UserDTO> userList) {
//
//        Assertions.assertThrows(UsernameNotFoundException.class,
//                () -> userService.modifyMultiUser(userList)
//        );
//    }
//
//    @DisplayName("다중 유저 수정 예외 테스트 - 빈 값이 전해졌을 때")
//    @Test
//    @Transactional
//    void modifyMultiUserInvalidTest2() {
//
//        Assertions.assertThrows(InvalidInfoException.class,
//                () -> userService.modifyMultiUser(null)
//        );
//    }
//
//    @DisplayName("유저 탈퇴 테스트 - 정상 동작")
//    @ParameterizedTest
//    @ValueSource(strings = {"9999999", "9999998"})
//    @Transactional
//    void deleteUserTest(String employeeNumber) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.deleteUser(employeeNumber)
//        );
//    }
//
//    @DisplayName("유저 재가입 테스트 - 정상 동작")
//    @ParameterizedTest
//    @ValueSource(strings = {"9999999", "9999998"})
//    @Transactional
//    void rejoinUserTest(String employeeNumber) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.rejoinUser(employeeNumber)
//        );
//    }
//
//    @DisplayName("유저 재가입 테스트 - 정상 동작")
//    @ParameterizedTest
//    @ValueSource(strings = {"9999999", "9999998"})
//    @Transactional
//    void rejoinUserTes(String employeeNumber) {
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.rejoinUser(employeeNumber)
//        );
//    }
//
//    @DisplayName("가입 시 입력 정보 검증 테스트 - 정상 동작")
//    @ParameterizedTest
//    @MethodSource("duplicateInfoCheckNormalData")
//    @Transactional
//    void duplicateInfoCheckTest1(UserDTO userDTO) {
//
//        // 각각 이메일, 사번, 휴대폰 번호가 중복되는 경우에 Pass 이외의 에러 문구 발생
//        Assertions.assertEquals("Pass", userService.duplicateInfoCheck(userDTO)
//        );
//    }
//
//    @DisplayName("가입 시 입력 정보 검증 테스트 - 정상 동작(중복 정보 입력시)")
//    @ParameterizedTest
//    @MethodSource("duplicateInfoCheckDuplicateData")
//    @Transactional
//    void duplicateInfoCheckTest2(UserDTO userDTO) {
//
//        // 각각 이메일, 사번, 휴대폰 번호가 중복되는 경우에 Pass 이외의 에러 문구 발생
//        Assertions.assertNotEquals("Pass", userService.duplicateInfoCheck(userDTO)
//        );
//    }
//
//    @DisplayName("다중 유저 등록 시 입력 정보 중복 테스트 - 정상 동작")
//    @Test
//    void multiInfoInputCheckNormalTest() {
//
//        List<UserDTO> userDTOList = new ArrayList<>();
//        UserDTO userDTO1 = UserDTO.builder()
//                .email("testemail@gmail.com")
//                .employeeNumber("2401001")
//                .phoneNumber("010-1111-2222")
//                .build();
//        UserDTO userDTO2 = UserDTO.builder()
//                .email("testemai2@gmail.com")
//                .employeeNumber("2402001")
//                .phoneNumber("010-2222-2222")
//                .build();
//
//        userDTOList.add(userDTO1);
//        userDTOList.add(userDTO2);
//
//        Assertions.assertEquals("P", userService.multiInfoInputCheck(userDTOList));
//    }
//
//    @DisplayName("다중 유저 등록 시 입력 정보 중복 테스트 - 정상 동작(중복 정보 존재시)")
//    @Test
//    void multiInfoInputCheckInvalidTest() {
//
//        List<UserDTO> userDTOList = new ArrayList<>();
//        List<UserDTO> userDTOList2 = new ArrayList<>();
//        List<UserDTO> userDTOList3 = new ArrayList<>();
//        UserDTO userDTO1 = UserDTO.builder()
//                .email("testemail@gmail.com")
//                .employeeNumber("2401001")
//                .phoneNumber("010-1111-2222")
//                .build();
//        UserDTO userDTO2 = UserDTO.builder()
//                .email("testemail@gmail.com")
//                .employeeNumber("2402001")
//                .phoneNumber("010-2222-2222")
//                .build();
//        UserDTO userDTO3 = UserDTO.builder()
//                .email("testemail2@gmail.com")
//                .employeeNumber("2401001")
//                .phoneNumber("010-2222-2222")
//                .build();
//        UserDTO userDTO4 = UserDTO.builder()
//                .email("testemail2@gmail.com")
//                .employeeNumber("2402001")
//                .phoneNumber("010-1111-2222")
//                .build();
//
//        userDTOList.add(userDTO1);
//        userDTOList.add(userDTO2);
//
//        userDTOList2.add(userDTO1);
//        userDTOList2.add(userDTO3);
//
//        userDTOList3.add(userDTO1);
//        userDTOList3.add(userDTO4);
//
//        Assertions.assertEquals("NP", userService.multiInfoInputCheck(userDTOList));
//        Assertions.assertEquals("NP", userService.multiInfoInputCheck(userDTOList2));
//        Assertions.assertEquals("NP", userService.multiInfoInputCheck(userDTOList3));
//    }
//
//
//    @DisplayName("성별 변경 테스트 - 정상 동작")
//    @Test
//    @Transactional
//    void changeGenderTest() {
//
//        Assertions.assertFalse(userService.changeGender(1));
//        Assertions.assertTrue(userService.changeGender(3));
//    }
//
//    @DisplayName("비밀번호 리셋 테스트 - 정상 동작")
//    @Test
//    @Transactional
//    void resetPwdTest() {
//        Map<String, String> test = new HashMap<>();
//        test.put("email", "gridpeople2024@gmail.com");
//        test.put("pwd", "qwer1234!");
//
//        Assertions.assertDoesNotThrow(
//                () -> userService.resetPwd(test)
//        );
//    }
//
//}
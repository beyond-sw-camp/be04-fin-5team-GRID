package org.highfives.grid.user.command.service;

import jakarta.transaction.Transactional;
import org.highfives.grid.user.command.aggregate.Gender;
import org.highfives.grid.user.command.aggregate.JoinType;
import org.highfives.grid.user.command.aggregate.WorkType;
import org.highfives.grid.user.command.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class UserServiceImplTests {

    @Autowired
    private UserServiceImpl userService;

    static Stream<Arguments> addUserDTO() {
        return Stream.of(
                Arguments.of(new UserDTO(
                        "testemail@gamil.com","testpwd","tester1",
                        "2499999", Gender.M,"010-1234-5678",
                        "2024-05-06", JoinType.NEW, WorkType.R, "2024-05-06",
                        4,1,1,1)
                )
        );
    }

    static Stream<Arguments> userList() {
        List<UserDTO> userDTO1 = new ArrayList<>();

        userDTO1.add(new UserDTO("testemail@gamil.com","testpwd","tester1",
                "2499999", Gender.M,"010-1234-5678",
                "2024-05-06", JoinType.NEW, WorkType.R, "2024-05-06",
                4,1,1,1));

        userDTO1.add(new UserDTO("testemail2@gamil.com","testpwd","tester1",
                "2399999", Gender.M,"010-5678-1234",
                "2024-05-06", JoinType.NEW, WorkType.R, "2024-05-06",
                4,1,1,1));

        return Stream.of(
                Arguments.of(userDTO1)
        );
    }

    @DisplayName("유저 추가")
    @ParameterizedTest
    @MethodSource("addUserDTO")
    @Transactional
    void addNewUserTest(UserDTO givenInfo) {

        Assertions.assertDoesNotThrow(
                () -> userService.addNewUser(givenInfo)
        );
    }

    @DisplayName("다중 유저 추가")
    @ParameterizedTest
    @MethodSource("userList")
    @Transactional
    void addMultiUserTest(List<UserDTO> givenInfo) {

        Assertions.assertDoesNotThrow(
                () -> userService.addMultiUser(givenInfo)
        );
    }

    @DisplayName("유저 수정")
    @Test
    @Transactional
    void modifyUserTest() {

        Assertions.assertDoesNotThrow(
                () -> userService.modifyUser(1, new UserDTO(1, "test@gmail.com"))
        );
    }

}
package org.highfives.grid.user.command.service;

import jakarta.transaction.Transactional;
import org.highfives.grid.user.command.aggregate.Gender;
import org.highfives.grid.user.command.aggregate.JoinType;
import org.highfives.grid.user.command.aggregate.WorkType;
import org.highfives.grid.user.command.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    static Stream<Arguments> addUserDTO() {

        return Stream.of(
                Arguments.of(new UserDTO(
                        "testemail@gamil.com","testpwd","tester1",
                        2499999, Gender.M,"010-1234-5678",
                        "2024-05-06", JoinType.NEW, WorkType.R, "2024-05-06",
                        4,1,1,1)
                )
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


}
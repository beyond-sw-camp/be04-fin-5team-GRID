package org.highfives.grid.user.query.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTests {

    @Autowired
    private UserServiceImpl userService;

    @DisplayName("팀장, 부서장 정보 조회")
    @Test
    void findLeaderInfoTest() {

        int id = 2;
        Assertions.assertDoesNotThrow(
                () -> userService.findLeaderInfo(id)
        );
    }

    @DisplayName("전체 유저 리스트 조회")
    @Test
    void findAllUsersTest() {
        Assertions.assertDoesNotThrow(
                () -> userService.findAllUsers()
        );
    }

    @DisplayName("이름으로 유저 리스트 조회")
    @Test
    void findUsersByName() {
        Assertions.assertDoesNotThrow(
                () -> userService.findUsersByName("Admin")
        );
    }

    @DisplayName("사원 번호로 유저 정보 조회")
    @Test
    void findUserByEmployeeNumberTest() {
        Assertions.assertDoesNotThrow(
                () -> userService.findUserByEmployeeNum("1")
        );
    }

    @DisplayName("id로 유저 정보 조회")
    @Test
    void findUserByIdTest() {
        Assertions.assertDoesNotThrow(
                () -> userService.findUserById(1)
        );
    }

    @DisplayName("email로 유저 정보 조회")
    @Test
    void checkNameByEmailTest() {
        Assertions.assertDoesNotThrow(
                () -> userService.checkNameByEmail("gridpeople2024@gmail.com")
        );
    }
}
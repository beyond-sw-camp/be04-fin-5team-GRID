//package org.highfives.grid.vacation.query.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class VacationServiceTest {
//
//    @Autowired
//    private VacationService vacationService;
//
//    @Test
//    @DisplayName("휴가 전체 조회 테스트")
//    void getAllVacations() {
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.getAllVacations()
//        );
//    }
//
//    @Test
//    @DisplayName("특정 회원의 휴가 조회 테스트")
//    void getUserVacations() {
//        //when
//        int employeeId = 2;
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.getUserVacations(employeeId)
//        );
//    }
//
//    @Test
//    @DisplayName("정책 조회 테스트")
//    void getVacationPolicy() {
//        //when
//        int typeId = 1;
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.getVacationPolicy(typeId)
//        );
//    }
//
//    @Test
//    @DisplayName("휴가 기록 전체 조회 테스트")
//    void getAllVacationHistory() {
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.getAllVacationHistory()
//        );
//    }
//
//    @Test
//    @DisplayName("휴가 정보 이름으로 검색 테스트")
//    void searchVacationInfoByName() {
//        //given
//        String name = "홍길동";
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.searchVacationInfoByName(name)
//        );
//    }
//
//    @Test
//    @DisplayName("휴가 정보 부서로 검색 테스트")
//    void searchVacationInfoByDept() {
//        //given
//        String dept = "인사";
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.searchVacationInfoByDept(dept)
//        );
//    }
//
//    @Test
//    @DisplayName("휴가 기록 검색 테스트")
//    void searchVacationHistory() {
//        //given
//        int typeId = 3;
//        int changeTypeId = 3;
//        int employeeId = 4;
//        //then
//        Assertions.assertDoesNotThrow(
//                () -> vacationService.searchVacationHistory(typeId, changeTypeId, employeeId)
//        );
//    }
//
//
//
//
//
//
//
//}
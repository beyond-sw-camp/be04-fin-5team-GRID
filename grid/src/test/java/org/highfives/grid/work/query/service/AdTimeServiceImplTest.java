// package org.highfives.grid.work.query.service;
//
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// @SpringBootTest(value = "QueryAdTimeServiceImplTest")
// class AdTimeServiceImplTest {
//
//     @Autowired
//     private AdTimeServiceImpl adTimeService;
//
//     @DisplayName("직원id로 조회")
//     @Test
//     void findAdTimeByEmployeeId() {
//         int employeeId = 5;
//
//         Assertions.assertDoesNotThrow(
//                 () -> adTimeService.findAdTimeByEmployeeId(employeeId)
//         );
//     }
//
//     @DisplayName("전체 조회")
//     @Test
//     void findAdTimeAll() {
//         Assertions.assertDoesNotThrow(
//                 () -> adTimeService.findAdTimeAll()
//         );
//     }
//
//     @DisplayName("날짜 조회")
//     @Test
//     void findAdTimeByStartTime() {
//         Assertions.assertDoesNotThrow(
//                 () -> adTimeService.findAdTimeByStartTime("2024-05-27")
//         );
//     }
// }

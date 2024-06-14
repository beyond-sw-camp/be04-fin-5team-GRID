// package org.highfives.grid.work.command.service;
//
// import org.highfives.grid.work.common.dto.AdTimeDTO;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.Arguments;
// import org.junit.jupiter.params.provider.MethodSource;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.stream.Stream;
//
// @SpringBootTest(value = "CommandAdTimeServiceImplTest")
// class AdTimeServiceImplTest {
//
//     @Autowired
//     private AdTimeServiceImpl adTimeService;
//
//     static Stream<Arguments> addAdTimeDTO() {
//
//         return Stream.of(
//                 Arguments.of(new AdTimeDTO(
//                         "2024-05-07 08:40:30",
//                         2
//                 ))
//         );
//     }
//
//     static Stream<Arguments> addAdTimeDTO2() {
//
//         return Stream.of(
//                 Arguments.of(new AdTimeDTO(
//                         2,
//                         "2024-05-08 18:40:30"
//                 ))
//         );
//     }
//
//
//     @DisplayName("출근 체크")
//     @ParameterizedTest(name="출근 체크")
//     @MethodSource("addAdTimeDTO")
//     @Transactional
//     void addStartTime(AdTimeDTO adTimeDTO) {
//         Assertions.assertDoesNotThrow(
//                 () -> adTimeService.addStartTime(adTimeDTO)
//         );
//     }
//
//     @DisplayName("퇴근 체크")
//     @ParameterizedTest(name="퇴근 체크")
//     @MethodSource("addAdTimeDTO2")
//     @Transactional
//     void addEndTime(AdTimeDTO adTimeDTO) {
//         AdTimeDTO adTimeDTO2 = new AdTimeDTO("2024-05-08 08:40:30", 2);
//         adTimeService.addStartTime(adTimeDTO2);
//         Assertions.assertDoesNotThrow(
//                 () -> adTimeService.addEndTime(adTimeDTO)
//         );
//     }
//
//     @DisplayName("날짜와 직원 id로 조회")
//     @Test
//     void findAdTime() {
//         String date = "2024-05-27";
//         int employeeId = 5;
//
//         AdTimeDTO adTimeDTO = adTimeService.findAdTime(date, employeeId);
//
//         Assertions.assertNotNull(adTimeDTO);
//     }
//
// }
package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.AdTimeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest(value = "QueryAdTimeServiceImplTest")
class AdTimeServiceImplTest {

    @Autowired
    private AdTimeServiceImpl adTimeService;

    static Stream<Arguments> findAdTimeDTO() {

        return Stream.of(
                Arguments.of(new AdTimeDTO(
                        1,
                        "2024-05-07 08:40:30"
                ))
        );
    }

    @DisplayName("퇴근시간으로 기존 출근 조회")
    @ParameterizedTest
    @MethodSource("findAdTimeDTO")
    void findAdTime(AdTimeDTO adTimeDTO) {
        Assertions.assertDoesNotThrow(
                () -> adTimeService.findAdTimeByEmployeeIdAndStartTime(adTimeDTO)
        );
    }

    @DisplayName("직원id로 조회")
    @Test
    void findAdTimeByEmployeeId() {
        int employeeId = 1;
        Assertions.assertDoesNotThrow(
                () -> adTimeService.findAdTimeByEmployeeId(employeeId)
        );
    }

    @DisplayName("전체 조회")
    @Test
    void findAdTimeAll() {
        System.out.println("전체 조회");
        Assertions.assertDoesNotThrow(
                () -> adTimeService.findAdTimeAll()
        );
    }
}
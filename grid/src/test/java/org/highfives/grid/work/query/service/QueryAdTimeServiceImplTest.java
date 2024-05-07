package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.QueryAdTimeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
class QueryAdTimeServiceImplTest {

    @Autowired
    private QueryAdTimeServiceImpl queryAdTimeService;

    static Stream<Arguments> findAdTimeDTO() {

        return Stream.of(
                Arguments.of(new QueryAdTimeDTO(
                        1,
                        "2024-05-07 08:40:30"
                ))
        );
    }

    @DisplayName("퇴근시간으로 기존 출근 조회")
    @ParameterizedTest
    @MethodSource("findAdTimeDTO")
    void findAdTime(QueryAdTimeDTO queryAdTimeDTO) {
        Assertions.assertDoesNotThrow(
                () -> queryAdTimeService.findAdTimeByEmployeeIdAndStartTime(queryAdTimeDTO)
        );
    }
}
package org.highfives.grid.work.command.service;

import org.highfives.grid.work.common.dto.AdTimeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@SpringBootTest(value = "CommandAdTimeServiceImplTest")
class AdTimeServiceImplTest {

    @Autowired
    private AdTimeServiceImpl adTimeService;

    static Stream<Arguments> addAdTimeDTO() {

        return Stream.of(
                Arguments.of(new AdTimeDTO(
                        "2024-05-07 08:40:30",
                        1
                ))
        );
    }

    static Stream<Arguments> addAdTimeDTO2() {

        return Stream.of(
                Arguments.of(new AdTimeDTO(
                        1,
                        "2024-05-07 18:40:30"
                ))
        );
    }


    @DisplayName("출근 체크")
    @ParameterizedTest
    @MethodSource("addAdTimeDTO")
//    @Transactional
    void addStartTime(AdTimeDTO adTimeDTO) {
        Assertions.assertDoesNotThrow(
                () -> adTimeService.addStartTime(adTimeDTO)
        );
    }

    @DisplayName("퇴근 체크")
    @ParameterizedTest
    @MethodSource("addAdTimeDTO2")
    @Transactional
    void addEndTime(AdTimeDTO adTimeDTO) {
        System.out.println(adTimeDTO);
        Assertions.assertDoesNotThrow(
                () -> adTimeService.addEndTime(adTimeDTO)
        );
    }

}
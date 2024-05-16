package org.highfives.grid.work.query.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(value = "QueryAdTimeServiceImplTest")
class AdTimeServiceImplTest {

    @Autowired
    private AdTimeServiceImpl adTimeService;

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
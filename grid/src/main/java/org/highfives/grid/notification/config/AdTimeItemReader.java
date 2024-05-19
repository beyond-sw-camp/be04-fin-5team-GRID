package org.highfives.grid.notification.config;

import org.highfives.grid.work.command.aggregate.AdTime;
import org.highfives.grid.work.command.repository.AdTimeRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class AdTimeItemReader implements ItemReader<AdTime> {

    @Autowired
    private AdTimeRepository repository;

    @Override
    public AdTime read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1); // 예: 하루 전
        LocalDateTime endTime = LocalDateTime.now();

        List<AdTime> attendanceTimes = repository.findByCheckInTimeBetween(startTime, endTime);

        // 필요한 데이터 가공 후 반환
        // 예를 들어, 리스트의 첫 번째 요소 반환
        if (!attendanceTimes.isEmpty()) {
            return attendanceTimes.remove(0);
        } else {
            return null;
        }
    }
}

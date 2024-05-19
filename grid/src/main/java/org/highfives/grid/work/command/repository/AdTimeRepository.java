package org.highfives.grid.work.command.repository;

import org.apache.ibatis.annotations.Param;
import org.highfives.grid.work.command.aggregate.AdTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdTimeRepository extends JpaRepository<AdTime, Integer> {
    AdTime findByStartTimeAndEmployeeId(String startTime, int employeeId);

    @Query(value = "select adTime " +
                    "from AdTime adTime " +
                    "where adTime.startTime like concat(:startTime, '%') " +
                    "AND adTime.employeeId = :employeeId")
    AdTime findAdTimeByEmployeeIdAndEndTime(
            @Param("startTime") String startTime
            , @Param("employeeId") int employeeId);

    List<AdTime> findByCheckInTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}

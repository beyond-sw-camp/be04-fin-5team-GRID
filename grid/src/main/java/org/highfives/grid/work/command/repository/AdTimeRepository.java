package org.highfives.grid.work.command.repository;

import org.highfives.grid.work.command.aggregate.AdTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdTimeRepository extends JpaRepository<AdTime, Integer> {
    AdTime findByStartTimeAndEmployeeId(String startTime, int employeeId);
}

package org.highfives.grid.vacation.command.repository;

import org.highfives.grid.vacation.command.entity.VacationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationHistoryRepository extends JpaRepository<VacationHistory, Integer> {

}
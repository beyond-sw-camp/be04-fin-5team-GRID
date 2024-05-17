package org.highfives.grid.vacation.command.repository;

import org.highfives.grid.vacation.command.entity.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationTypeRepository extends JpaRepository<VacationType, Integer> {

}

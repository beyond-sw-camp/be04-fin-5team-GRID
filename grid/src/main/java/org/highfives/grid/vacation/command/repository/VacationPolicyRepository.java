package org.highfives.grid.vacation.command.repository;

import org.highfives.grid.vacation.command.entity.VacationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationPolicyRepository extends JpaRepository<VacationPolicy, Integer> {
}

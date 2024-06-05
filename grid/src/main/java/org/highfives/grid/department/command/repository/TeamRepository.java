package org.highfives.grid.department.command.repository;

import org.highfives.grid.department.command.aggregate.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository <Team, Integer>{

    boolean existsByLeaderId(int leaderId);
}

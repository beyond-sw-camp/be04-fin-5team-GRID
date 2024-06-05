package org.highfives.grid.department.command.repository;

import org.highfives.grid.department.command.aggregate.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {


    boolean existsByLeaderId(int leaderId);
}

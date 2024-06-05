package org.highfives.grid.user.command.repository;

import org.highfives.grid.user.command.aggregate.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Employee, Integer> {

    Employee findByEmployeeNumber(String employeeNumber);

    Employee findByEmail(String email);

    Employee findByPhoneNumber(String phoneNumber);

    List<Employee> findByDepartmentId(int departmentId);

    List<Employee> findByTeamId(int teamId);
}

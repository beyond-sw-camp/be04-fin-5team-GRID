package org.highfives.grid.notification.config;

import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.repository.UserRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmployeeItemReader implements ItemReader<Employee> {

    @Autowired
    private UserRepository userRepository;


    private List<Employee> employees;
    private int nextEmployeeIndex;

    @Override
    public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (employees == null) {
            employees = userRepository.findAll(); // Find all employees who should have checked in
        }
        Employee nextEmployee = null;
        if (nextEmployeeIndex < employees.size()) {
            nextEmployee = employees.get(nextEmployeeIndex);
            nextEmployeeIndex++;
        } else {
            employees = null; // Reset for the next execution
            nextEmployeeIndex = 0;
        }
        return nextEmployee;
    }
}

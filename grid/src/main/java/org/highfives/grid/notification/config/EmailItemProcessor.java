package org.highfives.grid.notification.config;

import org.highfives.grid.user.command.aggregate.Employee;
import org.springframework.batch.item.ItemProcessor;

public class EmailItemProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {

        return employee;
    }
}

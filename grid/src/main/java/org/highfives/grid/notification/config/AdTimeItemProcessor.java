package org.highfives.grid.notification.config;

import org.highfives.grid.work.command.aggregate.AdTime;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.repository.UserRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class AdTimeItemProcessor implements ItemProcessor<AdTime, Employee> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Employee process(AdTime adTime) throws Exception {
       return userRepository.findById(adTime.getEmployeeId()).orElse(null);
    }
}

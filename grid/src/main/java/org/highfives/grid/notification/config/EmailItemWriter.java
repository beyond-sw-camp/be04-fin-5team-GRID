package org.highfives.grid.notification.config;

import org.highfives.grid.user.command.aggregate.Employee;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailItemWriter implements ItemWriter<Employee> {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void write(Chunk<? extends Employee> chunk) throws Exception {
        for (Employee employee : chunk) {
            sendEmail(employee);
        }
    }

    private void sendEmail(Employee employee) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(employee.getEmail());
        message.setSubject(" 출근 체크 알림 ");
        message.setText("출근 체크가 안되어있습니다..");
        mailSender.send(message);
    }

}

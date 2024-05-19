package org.highfives.grid.notification.config;

import org.highfives.grid.work.command.aggregate.AdTime;
import org.highfives.grid.user.command.aggregate.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AdTimeItemReader reader;

    @Autowired
    private AdTimeItemProcessor processor;

    @Autowired
    private EmailItemWriter writer;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job emailJob() {
        return jobBuilderFactory.get("emailJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .<AdTime, Employee>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void runJob() throws Exception {
        jobLauncher.run(emailJob(), new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis()).toJobParameters());
    }
}

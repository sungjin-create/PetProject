package com.pet.petproject.common.batch.config;

import com.pet.petproject.common.batch.tasklet.BatchTasklet;
import com.pet.petproject.feed.check.service.FeedCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final FeedCheckService feedCheckService;

    @Bean
    public Job alarmJob() {
        return jobBuilderFactory.get("alarmJob")
                .start(feedAlarmStep())
                .build();
    }

    @Bean
    public Step feedAlarmStep() {
        return stepBuilderFactory.get("feedAlarmStep")
                .tasklet(new BatchTasklet(feedCheckService))
                .build();
    }

}

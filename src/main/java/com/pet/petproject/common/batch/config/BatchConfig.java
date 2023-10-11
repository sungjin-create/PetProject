package com.pet.petproject.common.batch.config;

import com.pet.petproject.common.batch.chunk.FeedCheckChunkProcess;
import com.pet.petproject.common.batch.chunk.HospitalCheckChunkProcess;
import com.pet.petproject.feed.check.entity.FeedCheck;
import com.pet.petproject.hospital.check.entity.HospitalCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final FeedCheckChunkProcess feedCheckChunkProcess;
  private final HospitalCheckChunkProcess hospitalCheckChunkProcess;
  private static final int chunkSize = 100;

  @Bean
  public Job feedAlarmJob() {
    return jobBuilderFactory.get("feedAlarmJob")
        .start(feedAlarmStep())
        .build();
  }

  @Bean
  public Job hospitalAlarmJob() {
    return jobBuilderFactory.get("hospitalAlarmJob")
        .start(hospitalAlarmStep())
        .build();
  }

  @Bean
  @JobScope
  public Step feedAlarmStep() {
    return stepBuilderFactory.get("feedAlarmStep")
        .<FeedCheck, FeedCheck>chunk(chunkSize)
        .reader(feedCheckItemReader())
        .writer(feedCheckItemWriter())
        .build();
  }

  @Bean
  @JobScope
  public Step hospitalAlarmStep() {
    return stepBuilderFactory.get("hospitalAlarmStep")
        .<HospitalCheck, HospitalCheck>chunk(chunkSize)
        .reader(hospitalCheckItemReader())
        .writer(hospitalCheckItemWriter())
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FeedCheck> feedCheckItemReader() {
    return feedCheckChunkProcess.feedCheckItemReader();
  }

  @Bean
  @StepScope
  public ItemWriter<FeedCheck> feedCheckItemWriter() {
    return feedCheckChunkProcess.feedCheckItemWriter();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<HospitalCheck> hospitalCheckItemReader() {
    return hospitalCheckChunkProcess.hospitalCheckItemReader();
  }

  @Bean
  @StepScope
  public ItemWriter<HospitalCheck> hospitalCheckItemWriter() {
    return hospitalCheckChunkProcess.hospitalCheckItemWriter();
  }
}

package com.pet.petproject.common.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {

  private final Job feedAlarmJob;
  private final Job hospitalAlarmJob;
  private final JobLauncher jobLauncher;

  @Scheduled(cron = "0 * * * * *")
  public void setAlarmJob() {
    try {
      jobLauncher.run(feedAlarmJob, new JobParametersBuilder()
          .addString("datetime", LocalDateTime.now().toString())
          .toJobParameters()  // job parameter 설정
      );
    } catch (JobExecutionException e) {
      e.printStackTrace();
    }
  }

  @Scheduled(cron = "0 0 12 * * *")
  public void setHospitalAlarmJob() {
    try {
      jobLauncher.run(hospitalAlarmJob, new JobParametersBuilder()
          .addString("datetime", LocalDateTime.now().toString())
          .toJobParameters()  // job parameter 설정
      );
    } catch (JobExecutionException e) {
      e.printStackTrace();
    }
  }
}

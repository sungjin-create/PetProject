package com.pet.petproject.common.batch.chunk;

import com.pet.petproject.common.sse.entity.NotificationType;
import com.pet.petproject.common.sse.service.NotificationService;
import com.pet.petproject.hospital.check.entity.HospitalCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HospitalCheckChunkProcess {

  private final EntityManagerFactory entityManagerFactory;
  private final NotificationService notificationService;
  private static final int PAGE_SIZE = 100;

  public JpaPagingItemReader<HospitalCheck> hospitalCheckItemReader() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("alarmTime", getTheCurrentTimeInMinutes());

    return new JpaPagingItemReaderBuilder<HospitalCheck>()
        .name("hospitalCheckJpaPagingItemReader")
        .entityManagerFactory(entityManagerFactory)
        .pageSize(PAGE_SIZE)
        .queryString("SELECT p FROM HospitalCheck p WHERE alarmTime = :alarmTime")
        .parameterValues(parameters)
        .build();
  }

  private static LocalDateTime getTheCurrentTimeInMinutes() {
    return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
  }

  @Transactional
  public ItemWriter<HospitalCheck> hospitalCheckItemWriter() {
    return items -> {
      for (HospitalCheck hospitalCheck : items) {
        notificationService.send(hospitalCheck.getPet().getMember(), NotificationType.FEED,
            makeSendContents(hospitalCheck));
      }
    };
  }

  private String makeSendContents(HospitalCheck hospitalCheck) {
    return "펫 : " + hospitalCheck.getPet().getPetName() +
        "의 Hospital visit 시각은 " + hospitalCheck.getAlarmTime() + " 입니다";
  }
}

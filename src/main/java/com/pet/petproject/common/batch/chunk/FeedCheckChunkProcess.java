package com.pet.petproject.common.batch.chunk;

import com.pet.petproject.common.sse.entity.NotificationType;
import com.pet.petproject.common.sse.service.NotificationService;
import com.pet.petproject.feed.check.entity.FeedCheck;
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
public class FeedCheckChunkProcess {

  private final EntityManagerFactory entityManagerFactory;
  private final NotificationService notificationService;
  private static final int PAGE_SIZE = 100;

  public JpaPagingItemReader<FeedCheck> feedCheckItemReader() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("alarmTime", getTheCurrentTimeInMinutes());

    return new JpaPagingItemReaderBuilder<FeedCheck>()
        .name("feedCheckJpaPagingItemReader")
        .entityManagerFactory(entityManagerFactory)
        .pageSize(PAGE_SIZE)
        .queryString("SELECT p FROM FeedCheck p WHERE alarmTime = :alarmTime")
        .parameterValues(parameters)
        .build();
  }

  private static LocalDateTime getTheCurrentTimeInMinutes() {
    return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
  }

  @Transactional
  public ItemWriter<FeedCheck> feedCheckItemWriter() {
    return items -> {
      for (FeedCheck feedCheck : items) {
        notificationService.send(feedCheck.getPet().getMember(), NotificationType.FEED,
            makeSendContents(feedCheck));
      }
    };
  }

  private String makeSendContents(FeedCheck feedCheck) {
    return "펫 : " + feedCheck.getPet().getPetName() +
        "의 feedingTime은 " + feedCheck.getAlarmTime() + " 입니다";
  }
}

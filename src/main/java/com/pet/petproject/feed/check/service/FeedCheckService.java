package com.pet.petproject.feed.check.service;

import com.pet.petproject.common.sse.entity.NotificationType;
import com.pet.petproject.common.sse.service.NotificationService;
import com.pet.petproject.feed.check.entity.FeedCheck;
import com.pet.petproject.feed.check.repository.FeedCheckRepository;
import com.pet.petproject.feed.feed.entity.Feed;
import com.pet.petproject.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCheckService {

  private final FeedCheckRepository feedCheckRepository;
  private final NotificationService notificationService;

  public void registerCheckList(Feed feed) {
    //시작날짜와 끝날짜를 사용하여 총 duration 계산
    LocalDateTime startDateTime = feed.getStartDay().atStartOfDay();
    LocalDateTime endDateTime = feed.getEndDay().atStartOfDay();
    Duration duration = Duration.between(startDateTime, endDateTime);

    //duration을 feedCycleDay로 나누어 사이클횟수 계산
    long feedCycleDay = feed.getFeedCycleDay();
    long diffDay = getDiffDay(duration, feedCycleDay);

    //연산 횟수가 많으므로 변수로 저장
    LocalTime feedCycleTime = feed.getFeedCycleTime();
    LocalTime alarmCycleTime = feed.getAlarmTime();
    LocalDateTime now = LocalDateTime.now();

    //모든 FeedCheck를 List형태로 저장
    List<FeedCheck> feedCheckList = new ArrayList<>();

    for (long i = 0; i <= diffDay; i++) {
      //먹이를 주는 시간 계산
      LocalDateTime feedTime = startDateTime.plusDays(i * feedCycleDay).plusHours(feedCycleTime.getHour())
              .plusMinutes(feedCycleTime.getMinute());
      //알람을 주는 시간 계산
      LocalDateTime alarmTime = startDateTime.plusDays(i * feedCycleDay).plusHours(alarmCycleTime.getHour())
              .plusMinutes(alarmCycleTime.getMinute());
      //feedCheckList에 저장
      feedCheckList.add(FeedCheck.builder()
              .feed(feed)
              .feedAmount(feed.getFeedAmount())
              .feedTime(feedTime)
              .alarmTime(alarmTime)
              .feedCheck(false)
              .registerDate(now).build());
    }
    //FeedRepository에 리스트에 담긴 FeedCheck저장
    feedCheckRepository.saveAll(feedCheckList);
  }

  private static long getDiffDay(Duration duration, long feedCycleDay) {
    return duration.getSeconds() / (24 * 60 * 60) / feedCycleDay;
  }

  //FeedAlarm 구현
  public void feedAlarm() {

    //현재시간을 바탕으로 매분마다 해당하는 시간으로 저장된 FeedCheck를 가져온다
    List<FeedCheck> feedCheckList = feedCheckRepository.findByAlarmTime(getAlarmTime());

    //해당하는 알람이 없는경우 return
    if (feedCheckList == null) {
      return;
    }

    //feedCheckList에 해당하는 알림을 보낸다.
    for (FeedCheck feedCheck : feedCheckList) {

      Member member = feedCheck.getFeed().getPet().getMember();
      //content example) 펫 : 고양이의 feedingTime은 2023-10-04T21:20 입니다.
      String contents = "펫 : " + feedCheck.getFeed().getPet().getPetName() +
              "의 feedingTime은 " + feedCheck.getAlarmTime() + " 입니다";

      notificationService.send(member, NotificationType.FEED, contents);
    }
  }

  private static LocalDateTime getAlarmTime() {
    LocalDateTime now = LocalDateTime.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(),
            now.getDayOfMonth(), now.getHour(), now.getMinute());
  }
}

package com.pet.petproject.feed.feed.controller;

import com.pet.petproject.feed.feed.model.FeedDto;
import com.pet.petproject.feed.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet/feed")
public class FeedController {

  private final FeedService feedService;

  /**
   * feed param example
   *     "petId" : 1,
   *     "feedAmount" : 30,
   *     "feedCycleDay" : 2, (2-> 이틀에 한번씩, 3->3일에 한번씩)
   *     "feedCycleTime" : "21:20", (feed를 주는 시간)
   *     "startDay" : "2023-10-04",
   *     "endDay" : "2024-11-20",
   *     "alarmTime" : "21:10
   *  2023년 10월 04일 21:20부터 2024년 11월 20일 21:20까지 2일마다 먹이주기 등록,
   *  알람시간 21:10분
   */
  @PostMapping("/register")
  public ResponseEntity<?> registerFeed(@RequestBody FeedDto feedDto) {
    feedService.registerFeedCycle(feedDto);
    return ResponseEntity.ok().build();
  }

  //feed삭제
  @DeleteMapping
  public ResponseEntity<?> deleteFeed(@RequestParam("feedId") Long feedId) {
    feedService.deleteFeedCheck(feedId);
    return ResponseEntity.ok().build();
  }
}

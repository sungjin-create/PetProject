package com.pet.petproject.common.sse.controller;

import com.pet.petproject.common.sse.service.NotificationService;
import com.pet.petproject.common.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * Client와 최초 Connection을 하기 위한 로직 Last-Event-ID : 이전에 받지 못한 이벤트가 존재하는 경우(SSE연결에 대한 시간 만료 혹은 종료),
   * 받은 마지막 이벤트 ID값을 넘겨 그 이후의 데이터(받지 못한 데이터)부터 받을 수 있게 할 수 있는 정보를 의미한다.
   */
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public ResponseEntity<SseEmitter> subscribe(@RequestHeader(value = "Last-Event-ID",
      required = false, defaultValue = "") String lastEventId) {
    String id = SpringSecurityUtil.getLoginId();
    return ResponseEntity.ok().body(notificationService.subscribe(id, lastEventId));
  }

}

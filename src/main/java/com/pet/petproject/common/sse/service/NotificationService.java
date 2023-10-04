package com.pet.petproject.common.sse.service;

import com.pet.petproject.common.sse.emitter.repository.EmitterRepository;
import com.pet.petproject.common.sse.entity.Notification;
import com.pet.petproject.common.sse.entity.NotificationType;
import com.pet.petproject.common.sse.model.NotificationResponseDto;
import com.pet.petproject.common.sse.repository.NotificationRepository;
import com.pet.petproject.member.entity.Member;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private static final Long timeout = 60L * 1000 * 60;

    public SseEmitter subscribe(String memberId, String lastEventId) {

        //데이터 유실지점을 파악하기 위해 로그인 아이디 + 현재시간을 기반으로 emitterId를 만든다
        String emitterId = makeTimeIncludeId(memberId);

        //SSE emitter는 emitterId를 기반으로 만들어 각 아이디마다 다른 emitter를 사용한다
        //유효시간이 지나면 Client에서 자동으로 재연결 요청함
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

        //SseEmitter가 완료 혹은 타임아웃된 경우 자동으로 레포지토리에서 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        //연결시 Client에게 연결 완료 메세지 전송
        String eventId = makeTimeIncludeId(memberId);
        sendNotification(emitter, eventId, emitterId, "[connect complete Id: " + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }
        return emitter;
    }

    private String makeTimeIncludeId(String memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private void sendLostData(String lastEventId, String memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(memberId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }


    //연결된 클라이언트에게 데이터 전송을 위한 로직
    public void send(Member receiver, NotificationType notificationType, String contents) {

        //전송할 알림을 db에 저장
        Notification notification = notificationRepository
                .save(createNotification(receiver, notificationType, contents));

        //receiverId를 이용하여 eventId생성 -> sseEmitter로 전송되는 이벤트의 고유 식별자
        String receiverId = receiver.getId();
        String eventId = receiverId + "_" + System.currentTimeMillis();

        //수신자에 연결된 모든 SseEmitter객체를 가져온다
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);

        //emitters에 저장된 각 알림들을 전송
        emitters.forEach(
                (key, emitter) -> {
                    //eventCache에 데이터 저장
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.of(notification));
                }
        );
    }

    private Notification createNotification(Member receiver, NotificationType notificationType, String contents) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(contents)
                .build();
    }
}

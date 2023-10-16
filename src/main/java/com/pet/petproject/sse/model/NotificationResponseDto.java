package com.pet.petproject.sse.model;

import com.pet.petproject.sse.entity.Notification;
import com.pet.petproject.sse.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationResponseDto {

    private NotificationType notificationType;

    private String content;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationType(notification.getNotificationType())
                .content(notification.getContent())
                .build();
    }

}

package com.pet.petproject.hospital.check.model;

import com.pet.petproject.hospital.check.entity.HospitalCheck;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalCheckDto {

  private Long hospitalCheckId;
  private String petName;
  private LocalDateTime visitDay;
  private LocalDateTime alarmTime;
  private boolean visitCheck;
  private LocalDateTime registerDate;

  public static Page<HospitalCheckDto> of(Page<HospitalCheck> hospitalChecks) {
    return hospitalChecks.map(
        m -> HospitalCheckDto.builder()
            .hospitalCheckId(m.getId())
            .petName(m.getPet().getPetName())
            .visitDay(m.getVisitDay())
            .alarmTime(m.getAlarmTime())
            .visitCheck(m.isVisitCheck())
            .registerDate(m.getRegisterDate())
            .build()
    );
  }
}

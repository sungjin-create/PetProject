package com.pet.petproject.feed.feed.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pet.petproject.feed.feed.entity.Feed;
import com.pet.petproject.pet.entity.Pet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedDto {

  private Long petId;
  private Integer feedAmount;
  private Integer feedCycleDay;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime feedCycleTime;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDay;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDay;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime alarmTime;

  public static Feed of(FeedDto feedDto, Pet pet) {
    return Feed.builder()
        .pet(pet)
        .feedAmount(feedDto.getFeedAmount())
        .feedCycleDay(feedDto.getFeedCycleDay())
        .feedCycleTime(feedDto.getFeedCycleTime())
        .startDay(feedDto.getStartDay())
        .endDay(feedDto.getEndDay())
        .alarmTime(feedDto.getAlarmTime())
        .registerDate(LocalDateTime.now())
        .build();
  }

}

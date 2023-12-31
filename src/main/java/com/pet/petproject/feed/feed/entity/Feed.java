package com.pet.petproject.feed.feed.entity;

import com.pet.petproject.feed.feed.model.FeedDto;
import com.pet.petproject.pet.entity.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "feed_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "pet_id")
  private Pet pet;

  private Integer feedAmount;
  private Integer feedCycleDay;
  private LocalTime feedCycleTime;
  private LocalTime alarmTime;
  private LocalDate startDay;
  private LocalDate endDay;

  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static Feed of(FeedDto feedDto) {
    return Feed.builder()
        .feedAmount(feedDto.getFeedAmount())
        .feedCycleDay(feedDto.getFeedCycleDay())
        .feedCycleTime(feedDto.getFeedCycleTime())
        .startDay(feedDto.getStartDay())
        .endDay(feedDto.getEndDay())
        .alarmTime(feedDto.getAlarmTime())
        .build();
  }
}

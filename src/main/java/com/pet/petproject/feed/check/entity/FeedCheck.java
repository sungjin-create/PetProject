package com.pet.petproject.feed.check.entity;

import com.pet.petproject.feed.feed.entity.Feed;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedCheck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "feed_id")
  private Feed feed;
  private Integer feedAmount;
  private LocalDateTime alarmTime;
  private LocalDateTime feedTime;
  private boolean feedCheck;
  private LocalDateTime registerDate;

}

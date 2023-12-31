package com.pet.petproject.feed.check.entity;

import com.pet.petproject.feed.feed.entity.Feed;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.pet.petproject.pet.entity.Pet;
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
  @Column(name = "feed_check_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "feed_id")
  private Feed feed;

  @ManyToOne
  @JoinColumn(name = "pet_id")
  private Pet pet;

  private Integer feedAmount;
  private LocalDateTime alarmTime;
  private LocalDateTime feedTime;
  private boolean feedCheck;
  private LocalDateTime registerDate;

}

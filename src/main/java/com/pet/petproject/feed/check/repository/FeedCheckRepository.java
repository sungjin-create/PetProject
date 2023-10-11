package com.pet.petproject.feed.check.repository;

import com.pet.petproject.feed.check.entity.FeedCheck;
import com.pet.petproject.feed.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedCheckRepository extends JpaRepository<FeedCheck, Long> {

  List<FeedCheck> findByAlarmTime(LocalDateTime alarmTime);

  void deleteAllByFeed(Feed feed);
}

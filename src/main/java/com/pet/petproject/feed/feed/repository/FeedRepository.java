package com.pet.petproject.feed.feed.repository;

import com.pet.petproject.feed.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

  void deleteByFeedId(Long feedId);

}

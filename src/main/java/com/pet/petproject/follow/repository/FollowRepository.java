package com.pet.petproject.follow.repository;

import com.pet.petproject.follow.entity.Follow;
import com.pet.petproject.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowed(Member follower, Member followed);

  List<Follow> findAllByFollower(Member follower);

  Page<Follow> findAllByFollower(Member follower, Pageable pageable);

  Optional<Follow> findByFollowerAndFollowed(Member follower, Member followed);

}

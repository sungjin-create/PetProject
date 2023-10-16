package com.pet.petproject.follow.service;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.board.model.BoardResponseDto;
import com.pet.petproject.board.board.repository.BoardRepository;
import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.common.util.PageUtil;
import com.pet.petproject.follow.entity.Follow;
import com.pet.petproject.follow.model.FollowResponseDto;
import com.pet.petproject.follow.repository.FollowRepository;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;
  private final FollowRepository followRepository;

  @Transactional
  public void registerFollow(String followerId, String followedId) {
    Member follower = memberRepository.findById(followerId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Following"));
    Member followed = memberRepository.findById(followedId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Followed"));

    if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Already Follow Registered");
    }

    followRepository.save(Follow.builder()
        .follower(follower)
        .followed(followed)
        .registerDate(LocalDateTime.now())
        .build());
  }

  public Page<?> getFollowBoard(String memberId, Pageable pageable) {

    Member member = getMember(memberId);

    List<Follow> followList = followRepository.findAllByFollower(member);
    List<Board> boardList = new ArrayList<>();
    for (Follow follow : followList) {
      Member followed = follow.getFollowed();
      boardList.addAll(boardRepository.getAllByMemberAndOpenYnIsTrue(followed));
    }

    return PageUtil.listToPage(BoardResponseDto.listFrom(boardList), pageable);
  }

  private Member getMember(String memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Member"));
  }

  public Page<FollowResponseDto> getFollowList(String memberId, Pageable pageable) {
    Member member = getMember(memberId);
    Page<Follow> followPage = followRepository.findAllByFollower(member, pageable);
    return FollowResponseDto.ofPage(followPage);
  }

  @Transactional
  public void cancelFollow(String memberId, String followedId) {
    Member follower = getMember(memberId);
    Member followed = getMember(followedId);
    Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Follow"));
    followRepository.delete(follow);
  }
}

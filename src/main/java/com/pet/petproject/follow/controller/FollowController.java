package com.pet.petproject.follow.controller;

import com.pet.petproject.common.util.SpringSecurityUtil;
import com.pet.petproject.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/follow")
public class FollowController {

  private final FollowService followService;

  @GetMapping("/{followedId}")
  public ResponseEntity<?> registerFollow(@PathVariable("followedId") String followedId) {
    String followerId = SpringSecurityUtil.getLoginId();
    followService.registerFollow(followerId, followedId);
    return ResponseEntity.ok().build();
  }

  //follow된 사람들의 공개 게시판 조회
  @GetMapping("/board")
  public ResponseEntity<?> getFollowBoard(
      @PageableDefault(size = 10, sort = "registerDate", direction = Sort.Direction.DESC) Pageable pageable) {
    String memberId = SpringSecurityUtil.getLoginId();
    return ResponseEntity.ok(followService.getFollowBoard(memberId, pageable));
  }

  //follow list 조회
  @GetMapping("/list")
  public ResponseEntity<?> getFollowList(
      @PageableDefault(size = 10, sort = "registerDate", direction = Sort.Direction.DESC) Pageable pageable) {
    String memberId = SpringSecurityUtil.getLoginId();
    return ResponseEntity.ok(followService.getFollowList(memberId, pageable));
  }

  //follow 취소
  @DeleteMapping("/cancel")
  public ResponseEntity<?> followCancel(@RequestParam("followedId") String followedId) {
    String memberId = SpringSecurityUtil.getLoginId();
    followService.cancelFollow(memberId, followedId);
    return ResponseEntity.ok().build();
  }
}

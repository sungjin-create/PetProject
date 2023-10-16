package com.pet.petproject.board.comment.controller;

import com.pet.petproject.board.comment.model.CommentRegisterDto;
import com.pet.petproject.board.comment.service.CommentService;
import com.pet.petproject.common.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/comment")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/register")
  public ResponseEntity<?> registerComment(@RequestBody CommentRegisterDto parameter) {
    String memberId = SpringSecurityUtil.getLoginId();
    commentService.registerComment(memberId, parameter);
    return ResponseEntity.ok().build();
  }
}

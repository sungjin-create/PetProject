package com.pet.petproject.board.likes.controller;

import com.pet.petproject.board.likes.redisson.RedissonLockLikesFacade;
import com.pet.petproject.board.likes.service.LikesService;
import com.pet.petproject.common.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board/likes")
public class LikesController {

    private final LikesService likesService;
    private final RedissonLockLikesFacade redissonLockStockFacade;

    @GetMapping("/check")
    public ResponseEntity<?> checkLikes(@RequestParam("boardId") Long boardId) {
        String memberId = SpringSecurityUtil.getLoginId();

        redissonLockStockFacade.decrease(boardId, memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/uncheck")
    public ResponseEntity<?> uncheckLikes(@RequestParam("boardId") Long boardId) {
        String memberId = SpringSecurityUtil.getLoginId();
        likesService.uncheckLikes(boardId, memberId);
        return ResponseEntity.ok().build();
    }
}

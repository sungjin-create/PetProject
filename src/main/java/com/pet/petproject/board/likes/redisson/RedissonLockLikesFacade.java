package com.pet.petproject.board.likes.redisson;

import com.pet.petproject.board.likes.service.LikesService;
import com.pet.petproject.common.exception.AppException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockLikesFacade {
    private final RedissonClient redissonClient;
    private final LikesService likesService;

    public void decrease(Long boardId, String memberId) {
        RLock lock = redissonClient.getLock(String.valueOf(boardId));
        try {
            boolean available = lock.tryLock(20, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lock get Fail");
            }
            likesService.checkLikes(boardId, memberId);

        } catch (InterruptedException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
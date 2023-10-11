package com.pet.petproject.feed.feed.service;

import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.feed.check.repository.FeedCheckRepository;
import com.pet.petproject.feed.check.service.FeedCheckService;
import com.pet.petproject.feed.feed.entity.Feed;
import com.pet.petproject.feed.feed.model.FeedDto;
import com.pet.petproject.feed.feed.repository.FeedRepository;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final PetRepository petRepository;
  private final FeedRepository feedRepository;
  private final FeedCheckService feedCheckService;
  private final FeedCheckRepository feedCheckRepository;

  public void registerFeedCycle(FeedDto feedDto) {
    //petId로 pet정보 가져오기
    Pet pet = petRepository.findByPetId(feedDto.getPetId()).orElseThrow(
        () -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Not found Pet"));

    //feedDto를 Feed객체로 변환
    Feed feed = FeedDto.of(feedDto, pet);

    //feed db에 저장
    feedRepository.save(feed);

    //feed 정보를 바탕으로 feedCheck 생성
    feedCheckService.registerCheckList(feed);
  }

  @Transactional
  public void deleteFeedCheck(Long feedId) {
    Feed feed = feedRepository.findById(feedId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found FeedId"));
    feedCheckRepository.deleteAllByFeed(feed);
    feedRepository.deleteByFeedId(feedId);
  }
}

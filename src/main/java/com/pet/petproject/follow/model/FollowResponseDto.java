package com.pet.petproject.follow.model;

import com.pet.petproject.follow.entity.Follow;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponseDto {

  private Long followId;
  private String followed;
  private LocalDateTime registerDate;

  public static Page<FollowResponseDto> ofPage(Page<Follow> followPage) {
    return followPage.map(
        m -> FollowResponseDto.builder()
            .followId(m.getId())
            .followed(m.getFollowed().getName())
            .build()
    );
  }
}

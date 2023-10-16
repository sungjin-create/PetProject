package com.pet.petproject.board.board.model;

import com.pet.petproject.board.board.entity.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
public class BoardResponseDto {

  private Long boardId;
  private String title;
  private Long numberOfLikes;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static Page<BoardResponseDto> of(Page<Board> board) {
    return board.map(
        m -> BoardResponseDto.builder()
            .boardId(m.getId())
            .title(m.getTitle())
            .numberOfLikes(m.getNumberOfLikes())
            .registerDate(m.getRegisterDate())
            .updateDate(m.getUpdateDate())
            .build());
  }

  public static List<BoardResponseDto> listFrom(List<Board> boardList) {
    return boardList.stream().map(board -> BoardResponseDto.builder()
            .boardId(board.getId())
            .title(board.getTitle())
            .numberOfLikes(board.getNumberOfLikes())
            .registerDate(board.getRegisterDate())
            .updateDate(board.getUpdateDate()).build())
        .collect(Collectors.toList());
  }

}

package com.pet.petproject.board.board.model;

import com.pet.petproject.board.board.entity.Board;
import java.time.LocalDateTime;
import java.util.List;
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
public class BoardDto {

    private String writer;
    private String title;
    private String contents;
    private List<String> images;
    private Long numberOfLikes;
    private LocalDateTime registerDate;


    public static Page<BoardDto> of(Page<Board> boardPage) {
        return boardPage.map(
                m -> BoardDto.builder()
                        .writer(m.getMember().getName())
                        .title(m.getTitle())
                        .contents(m.getContents())
                        .images(m.getImages())
                        .numberOfLikes(m.getNumberOfLikes())
                        .registerDate(m.getRegisterDate())
                        .build()
        );
    }
}
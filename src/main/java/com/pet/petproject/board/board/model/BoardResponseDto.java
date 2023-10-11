package com.pet.petproject.board.board.model;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.comment.entity.Comment;
import com.pet.petproject.board.comment.model.CommentResponseDto;
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
public class BoardResponseDto {

    private Long boardId;
    private String title;
    private List<String> images;
    private boolean openYn;
    private Long numberOfLikes;
    private Page<CommentResponseDto> commentPage;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;

    public static BoardResponseDto of(Board board, Page<Comment> commentPage) {
        return BoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .images(board.getImages())
                .openYn(board.isOpenYn())
                .numberOfLikes(board.getNumberOfLikes())
                .commentPage(CommentResponseDto.of(commentPage))
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .deleteDate(board.getDeleteDate())
                .build();
    }

}

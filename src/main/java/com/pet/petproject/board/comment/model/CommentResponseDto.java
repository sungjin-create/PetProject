package com.pet.petproject.board.comment.model;

import com.pet.petproject.board.comment.entity.Comment;
import com.pet.petproject.member.entity.Member;
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
public class CommentResponseDto {

    private long commentId;
    private String writer;
    private String contents;
    private LocalDateTime registerDate;


    public static Page<CommentResponseDto> of(Page<Comment> commentPage) {
        return commentPage.map(
                m -> CommentResponseDto.builder()
                        .commentId(m.getId())
                        .writer(m.getMember().getName())
                        .contents(m.getContents())
                        .registerDate(m.getRegisterDate())
                        .build());
    }

    public static CommentResponseDto of(Comment comment, Member member) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .writer(member.getName())
                .contents(comment.getContents())
                .registerDate(comment.getRegisterDate())
                .build();
    }

}

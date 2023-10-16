package com.pet.petproject.board.comment.entity;


import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  private String contents;

  private LocalDateTime registerDate;

  public static Comment of(Member member, Board board, String contents) {

    return Comment.builder()
        .member(member)
        .board(board)
        .contents(contents)
        .registerDate(LocalDateTime.now())
        .build();

  }
}

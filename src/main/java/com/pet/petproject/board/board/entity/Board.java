package com.pet.petproject.board.board.entity;

import com.pet.petproject.board.board.model.BoardRegisterDto;
import com.pet.petproject.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String contents;
    private String uuid;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private List<String> images;
    private boolean openYn;
    private Long numberOfLikes;

    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;

    public static Board of(Member member, BoardRegisterDto parameter, String uuid) {

        return Board.builder()
                .member(member)
                .title(parameter.getTitle())
                .contents(parameter.getContents())
                .uuid(uuid)
                .openYn(parameter.isOpenYn())
                .numberOfLikes(0L)
                .registerDate(LocalDateTime.now())
                .build();
    }
}

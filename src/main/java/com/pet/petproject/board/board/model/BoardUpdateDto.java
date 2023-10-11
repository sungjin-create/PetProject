package com.pet.petproject.board.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateDto{

    private Long boardId;
    private String title;
    private String contents;
    private boolean openYn;

}

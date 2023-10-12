package com.pet.petproject.board.elasticsearch.entity;

import com.pet.petproject.board.board.entity.Board;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "board")
public class BoardDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String contents;

    @Field(type = FieldType.Long)
    private Long numberOfLikes;

    public static BoardDocument from(Board board) {
        return BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .numberOfLikes(board.getNumberOfLikes())
                .build();
    }

}

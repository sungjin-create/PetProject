package com.pet.petproject.todo.model;

import com.pet.petproject.todo.entity.Todo;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoResponseDto {

  private Long todoId;
  private String petName;
  private String contents;
  private LocalDate targetDate;
  private boolean todoCheck;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static Page<TodoResponseDto> of(Page<Todo> todos) {
    return todos.map(
        m -> TodoResponseDto.builder()
            .todoId(m.getId())
            .petName(m.getPet().getPetName())
            .contents(m.getContents())
            .targetDate(m.getTargetDate())
            .todoCheck(m.isTodoCheck())
            .registerDate(m.getRegisterDate())
            .updateDate(m.getUpdateDate())
            .build()
    );
  }

}

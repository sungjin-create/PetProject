package com.pet.petproject.todo.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoUpdateDto extends TodoRegisterDto {

  private Long todoId;
  private boolean todoCheck;
}

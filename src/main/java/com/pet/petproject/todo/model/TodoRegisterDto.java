package com.pet.petproject.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRegisterDto {

  private Long petId;
  private String contents;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate targetDate;
}

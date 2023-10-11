package com.pet.petproject.todo.entity;

import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.todo.model.TodoRegisterDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "todo_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "pet_id")
  private Pet pet;

  private String contents;
  private LocalDate targetDate;
  private boolean todoCheck;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static Todo of(TodoRegisterDto parameter, Pet pet) {
    return Todo.builder()
        .pet(pet)
        .contents(parameter.getContents())
        .targetDate(parameter.getTargetDate())
        .todoCheck(false)
        .registerDate(LocalDateTime.now())
        .build();
  }
}

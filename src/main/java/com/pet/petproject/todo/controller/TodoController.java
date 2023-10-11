package com.pet.petproject.todo.controller;

import com.pet.petproject.common.util.SpringSecurityUtil;
import com.pet.petproject.todo.model.TodoRegisterDto;
import com.pet.petproject.todo.model.TodoUpdateDto;
import com.pet.petproject.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet/todo")
public class TodoController {

  private final TodoService todoService;

  @GetMapping("/register")
  public ResponseEntity<?> registerTodo(@RequestBody TodoRegisterDto parameter) {
    todoService.registerTodo(parameter);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateTodo(
      @RequestBody TodoUpdateDto parameter) {
    todoService.updateTodo(parameter);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteTodo(@RequestParam Long todoId) {
    todoService.deleteTodo(todoId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/check")
  public ResponseEntity<?> checkingTodo(@RequestParam Long todoId) {
    todoService.checkingTodo(todoId);
    return ResponseEntity.ok().build();
  }

  //checking하지 않은 Todo리스트 Pet별로 가져오기
  @GetMapping("/uncheck")
  public ResponseEntity<?> getUncheckTodo(@RequestParam Long petId,
      @PageableDefault(size = 10, sort = "targetDate", direction = Sort.Direction.ASC) Pageable pageable) {

    return ResponseEntity.ok().body(todoService.getUncheckTodo(petId, pageable));
  }

  //checking하지 않은 Todo리스트 전체 가져오기
  @GetMapping("/uncheck/all")
  public ResponseEntity<?> getUncheckTodoAll(@PageableDefault
      (size = 10, sort = "targetDate", direction = Sort.Direction.ASC) Pageable pageable) {
    String memberId = SpringSecurityUtil.getLoginId();
    return ResponseEntity.ok().body(todoService.getUncheckTodoAll(memberId, pageable));
  }
}

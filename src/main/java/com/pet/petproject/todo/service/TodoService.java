package com.pet.petproject.todo.service;

import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.repository.PetRepository;
import com.pet.petproject.todo.entity.Todo;
import com.pet.petproject.todo.model.TodoRegisterDto;
import com.pet.petproject.todo.model.TodoResponseDto;
import com.pet.petproject.todo.model.TodoUpdateDto;
import com.pet.petproject.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final PetRepository petRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void registerTodo(TodoRegisterDto parameter) {
    Pet pet = getPet(parameter.getPetId());

    todoRepository.save(Todo.of(parameter, pet));
  }

  private Pet getPet(Long petId) {
    return petRepository.findById(petId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Pet"));
  }

  @Transactional
  public void updateTodo(TodoUpdateDto parameter) {
    Pet pet = getPet(parameter.getPetId());

    Todo todo = getTodo(parameter.getTodoId());

    todo.setPet(pet);
    todo.setContents(parameter.getContents());
    todo.setTodoCheck(parameter.isTodoCheck());
    todo.setUpdateDate(LocalDateTime.now());
    todo.setTargetDate(parameter.getTargetDate());

  }

  private Todo getTodo(Long todoId) {
    return todoRepository.findById(todoId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Todo"));
  }

  @Transactional
  public void deleteTodo(Long todoId) {
    Todo todo = todoRepository.findById(todoId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Todo"));

    todo.setDeleteDate(LocalDateTime.now());
  }

  //Todo checking
  @Transactional
  public void checkingTodo(Long todoId) {
    Todo todo = getTodo(todoId);
    todo.setTodoCheck(true);
    todo.setUpdateDate(LocalDateTime.now());
  }

  //체크하지않은 Todo pet별로 가져오기
  public Page<TodoResponseDto> getUncheckTodo(Long petId, Pageable pageable) {
    Pet pet = getPet(petId);

    Page<Todo> todoPage = todoRepository.findAllByPetAndTodoCheckIsFalseAndDeleteDateIsNull(pet,
        pageable);

    return TodoResponseDto.of(todoPage);
  }

  //체크하지 않은 모든 Todo 가져오기
  public Page<Todo> getUncheckTodoAll(String memberId, Pageable pageable) {
    Member member = memberRepository.findById(memberId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Member"));

    List<Pet> petList = petRepository.findAllByMember(member);
    List<Todo> todoList = new ArrayList<>();
    for (Pet pet : petList) {
      todoList.addAll(todoRepository.findAllByPetAndTodoCheckIsFalseAndDeleteDateIsNull(pet));
    }

    //List를 페이징처리
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), todoList.size());

    return new PageImpl<>(todoList.subList(start, end), pageable, todoList.size());
  }
}

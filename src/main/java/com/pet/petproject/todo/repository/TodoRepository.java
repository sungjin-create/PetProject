package com.pet.petproject.todo.repository;

import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

  Page<Todo> findAllByPetAndTodoCheckIsFalseAndDeleteDateIsNull(Pet pet, Pageable pageable);

  List<Todo> findAllByPetAndTodoCheckIsFalseAndDeleteDateIsNull(Pet pet);
}

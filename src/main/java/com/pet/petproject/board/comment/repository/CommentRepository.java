package com.pet.petproject.board.comment.repository;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByBoard(Board board, Pageable pageable);

}

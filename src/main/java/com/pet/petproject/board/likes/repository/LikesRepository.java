package com.pet.petproject.board.likes.repository;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.likes.entity.Likes;
import com.pet.petproject.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  boolean existsByMemberAndBoard(Member member, Board board);

  void deleteLikesByMemberAndBoard(Member member, Board board);

}

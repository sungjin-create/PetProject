package com.pet.petproject.board.board.repository;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  Page<Board> getAllByOpenYnIsTrue(Pageable pageable);

  Page<Board> getAllByMemberInAndOpenYnIsTrue(List<Member> followed, Pageable pageable);

}

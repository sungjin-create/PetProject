package com.pet.petproject.board.board.repository;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> getAllByOpenYnIsTrue(Pageable pageable);

    List<Board> getAllByMemberAndOpenYnIsTrue(Member member);

}

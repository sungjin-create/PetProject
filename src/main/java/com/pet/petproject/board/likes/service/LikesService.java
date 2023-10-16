package com.pet.petproject.board.likes.service;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.board.repository.BoardRepository;
import com.pet.petproject.board.likes.entity.Likes;
import com.pet.petproject.board.likes.repository.LikesRepository;
import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

  private final LikesRepository likesRepository;
  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void checkLikes(Long boardId, String memberId) {

    Member member = findMember(memberId);

    Board board = findBoard(boardId);

    if (likesRepository.existsByMemberAndBoard(member, board)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Already Check Likes");
    }

    board.setNumberOfLikes(board.getNumberOfLikes() + 1);

    likesRepository.save(Likes.builder()
        .member(member)
        .board(board)
        .registerDate(LocalDateTime.now())
        .build());
  }

  private Member findMember(String memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Member"));
    return member;
  }

  private Board findBoard(Long boardId) {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Board"));
    return board;
  }

  @Transactional
  public void uncheckLikes(Long boardId, String memberId) {
    Member member = findMember(memberId);
    Board board = findBoard(boardId);
    if (!likesRepository.existsByMemberAndBoard(member, board)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Check Likes");
    }
    board.setNumberOfLikes(board.getNumberOfLikes() - 1);
    likesRepository.deleteLikesByMemberAndBoard(member, board);
  }
}

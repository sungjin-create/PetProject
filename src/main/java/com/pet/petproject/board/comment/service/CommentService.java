package com.pet.petproject.board.comment.service;

import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.board.repository.BoardRepository;
import com.pet.petproject.board.comment.entity.Comment;
import com.pet.petproject.board.comment.model.CommentRegisterDto;
import com.pet.petproject.board.comment.repository.CommentRepository;
import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void registerComment(String memberId, CommentRegisterDto parameter) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Member"));
        Board board = boardRepository.findById(parameter.getBoardId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Board"));
        commentRepository.save(Comment.of(member, board, parameter.getContents()));
    }
}

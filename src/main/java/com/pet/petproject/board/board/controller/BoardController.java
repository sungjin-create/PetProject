package com.pet.petproject.board.board.controller;

import com.pet.petproject.board.board.model.BoardRegisterDto;
import com.pet.petproject.board.board.model.BoardUpdateDto;
import com.pet.petproject.board.board.service.BoardService;
import com.pet.petproject.common.util.SpringSecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/register")
    public ResponseEntity<?> boardRegister(@RequestPart("files") List<MultipartFile> imageList,
                                           @RequestPart("jsonBoardRegister") BoardRegisterDto parameter) {
        String memberId = SpringSecurityUtil.getLoginId();
        boardService.registerBoard(memberId, imageList, parameter);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> boardUpdate(@RequestPart("files") List<MultipartFile> imageList,
                                         @RequestPart("jsonBoardUpdate") BoardUpdateDto parameter) {
        boardService.updateBoard(imageList, parameter);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> boardDelete(@RequestParam("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }

    //최신순으로 정렬
    @GetMapping("/latest/order")
    public ResponseEntity<?> getByLatestOrder(
            @PageableDefault(size = 10, sort = "registerDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(boardService.getByLatestOrder(pageable));
    }

    //좋아요 순으로 정렬
    @GetMapping("/likes/order")
    public ResponseEntity<?> getByLikesOrder(
            @PageableDefault(size = 10, sort = "numberOfLikes", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(boardService.getByLikesOrder(pageable));
    }

    //elastic Search를 사용해 title중 검색
    @PostMapping("/search/title")
    public ResponseEntity<?> searchByTitle(@RequestParam("title") String title,
             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(boardService.searchByTitle(title, pageable));
    }

    //elastic Search를 사용해 contents중 검색
    @PostMapping("/search/contents")
    public ResponseEntity<?> searchByContents(@RequestParam("contents") String contents,
             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(boardService.searchByContents(contents, pageable));
    }

    //elastic Search를 사용해 title, contents 검색
    @PostMapping("/search/all")
    public ResponseEntity<?> searchByTitleAndContents(@RequestParam("keyword") String parameter,
             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(boardService.searchByContentsAndTitle(parameter, pageable));
    }

    //게시글 댓글 포함 상세 정보 가져오기
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId,
             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(boardService.getBoardDetail(boardId, pageable));
    }
}

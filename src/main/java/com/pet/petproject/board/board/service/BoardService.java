package com.pet.petproject.board.board.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pet.petproject.board.board.entity.Board;
import com.pet.petproject.board.board.model.BoardDto;
import com.pet.petproject.board.board.model.BoardRegisterDto;
import com.pet.petproject.board.board.model.BoardResponseDto;
import com.pet.petproject.board.board.model.BoardUpdateDto;
import com.pet.petproject.board.board.repository.BoardRepository;
import com.pet.petproject.board.comment.entity.Comment;
import com.pet.petproject.board.comment.repository.CommentRepository;
import com.pet.petproject.board.elasticsearch.entity.BoardDocument;
import com.pet.petproject.board.elasticsearch.repository.BoardSearchRepository;
import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final AmazonS3Client amazonS3Client;
    private final BoardSearchRepository boardSearchRepository;
    private final CommentRepository commentRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void registerBoard(String memberId, List<MultipartFile> imageList, BoardRegisterDto parameter) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Member"));

        String uuid = UUID.randomUUID().toString();

        //게시판 db에 저장
        Board board = boardRepository.save(Board.of(member, parameter, uuid));

        //게시판 es에 저장
        boardSearchRepository.save(BoardDocument.from(board));

        //이미지 s3에 저장
        List<String> list = registerBoardImageToS3(board, imageList);

        //이미지 db에 저장
        board.setImages(list);

    }

    public List<String> registerBoardImageToS3(Board board, List<MultipartFile> fileList) {

        int count = 1;
        List<String> imageUrlList = new ArrayList<>();

        for (MultipartFile file : fileList) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String fileName = file.getOriginalFilename();
            String extension = getExtension(fileName);

            String fileUrl = board.getUuid() + "images" + count++ + "." + extension;
            String key = "board/" + fileUrl;

            try {
                if (amazonS3Client.doesObjectExist(bucket, key)) {
                    amazonS3Client.deleteObject(bucket, key);
                }
                amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);
                imageUrlList.add(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageUrlList;
    }

    private static String getExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        return fileName.substring(extensionIndex + 1);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Board"));

        List<String> imageList = board.getImages();
        deleteImageFromS3(imageList);
        boardRepository.deleteById(boardId);
        boardSearchRepository.deleteById(boardId);
    }

    private void deleteImageFromS3(List<String> imageList) {
        for (String key : imageList) {
            if (amazonS3Client.doesObjectExist(bucket, key)) {
                amazonS3Client.deleteObject(bucket, key);
            }
        }
    }

    @Transactional
    public void updateBoard(List<MultipartFile> imageList, BoardUpdateDto parameter) {
        Board board = boardRepository.findById(parameter.getBoardId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Board"));

        board.setTitle(parameter.getTitle());
        board.setContents(parameter.getContents());
        board.setOpenYn(parameter.isOpenYn());
        board.setUpdateDate(LocalDateTime.now());

        deleteImageFromS3(board.getImages());
        List<String> images = registerBoardImageToS3(board, imageList);
        board.setImages(images);
    }

    public Page<BoardDto> getByLatestOrder(Pageable pageable) {
        Page<Board> boardPage = boardRepository.getAllByOpenYnIsTrue(pageable);
        return BoardDto.of(boardPage);
    }

    public Page<BoardDto> getByLikesOrder(Pageable pageable) {
        Page<Board> boardPage = boardRepository.getAllByOpenYnIsTrue(pageable);
        return BoardDto.of(boardPage);
    }

    public Page<BoardDocument> searchByTitle(String title, Pageable pageable) {
        return boardSearchRepository.findByTitle(title, pageable);
    }

    public Page<BoardDocument> searchByContents(String contents, Pageable pageable) {
        return boardSearchRepository.findByContents(contents, pageable);
    }

    public Page<BoardDocument> searchByContentsAndTitle(String parameter, Pageable pageable) {
        return boardSearchRepository.findByTitleOrContents(parameter, parameter, pageable);
    }

    public BoardResponseDto getBoardDetail(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Board"));
        Page<Comment> commentPage = commentRepository.findByBoard(board, pageable);
        return BoardResponseDto.of(board, commentPage);
    }
}

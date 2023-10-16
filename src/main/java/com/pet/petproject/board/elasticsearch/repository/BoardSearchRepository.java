package com.pet.petproject.board.elasticsearch.repository;

import com.pet.petproject.board.elasticsearch.entity.BoardDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BoardSearchRepository extends ElasticsearchRepository<BoardDocument, Long> {

  Page<BoardDocument> findByTitle(String title, Pageable pageable);

  Page<BoardDocument> findByContents(String contents, Pageable pageable);

  Page<BoardDocument> findByTitleOrContents(String keyword1, String keyword2, Pageable pageable);

}

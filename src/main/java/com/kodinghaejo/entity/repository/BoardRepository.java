package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	public List<BoardEntity> findByIsUseOrderByRegdateDesc(String isUse);
	
	//카테고리 제외
	List<BoardEntity> findByCatNot(String cat);
	
	//카테고리
	List<BoardEntity> findByCat(String cat);
	
	//검색
	List<BoardEntity> findByTitleContainingAndCat(String searchKeyword, String category);
		
	//공지사항 검색
	List<BoardEntity> findByTitleContainingAndCatNot(String searchKeyword, String category);
	
	public long countByCatAndRegdateBetween(String cat, LocalDateTime start, LocalDateTime end);
}
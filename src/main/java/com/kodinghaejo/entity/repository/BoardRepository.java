package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.MemberEntity;

import jakarta.transaction.Transactional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	//사용가능(isUse -> Y) 상태인 게시물 전체
	public List<BoardEntity> findByIsUseOrderByRegdateDesc(String isUse);
	

	//게시물 조회수 증가 --> Native SQL 
	@Transactional
	@Modifying
	@Query(value="UPDATE jpa_board SET hit_cnt = COALESCE(hit_cnt, 0) + 1 WHERE idx = :idx", nativeQuery = true)
	public void hitno(@Param("idx") Long idx);
	

	//카테고리 제외
	List<BoardEntity> findByCatNot(String cat);
	
	//카테고리
	List<BoardEntity> findByCat(String cat);
	
	//검색
	List<BoardEntity> findByTitleContainingAndCat(String searchKeyword, String category);
		
	//공지사항 검색
	List<BoardEntity> findByTitleContainingAndCatNot(String searchKeyword, String category);
	

	public long countByCatAndRegdateBetween(String cat, LocalDateTime start, LocalDateTime end);


	public Page<BoardEntity> findByEmailAndIsUse(MemberEntity email, String isUse, Pageable pageable);


}
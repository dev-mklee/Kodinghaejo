package com.kodinghaejo.entity.repository;

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
	
//	// 특정 페이지에 해당하는 게시글 목록 조회
//    @Query("SELECT b FROM board b ORDER BY b.regdate DESC")
//    List<BoardEntity> findBoards(@Param("offset") int offset, @Param("limit") int limit);
//    
//    // 전체 게시글 수 조회
//    @Query("SELECT COUNT(b) FROM board b")
//    int countAllBoards();
}
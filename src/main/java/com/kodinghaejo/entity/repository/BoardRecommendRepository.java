package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.BoardRecommendEntityId;

public interface BoardRecommendRepository extends JpaRepository<BoardRecommendEntity, BoardRecommendEntityId> {

	//게시글의 추천 수
	public Long countByBoardIdxAndGoodChk(BoardEntity boardEntity, String goodChk);
	
	//게시글의 신고 수
	public Long countByBoardIdxAndBadChk(BoardEntity boardEntity, String badChk);

	//실제 컬럼명인 B_IDX로 좋아요 개수 조회
	@Query(value = "SELECT COUNT(*) FROM jpa_board_recommend WHERE board_idx = :boardIdx and good_chk = 'Y'", nativeQuery = true)
	long countByBoardIdx(@Param("boardIdx") Long boardIdx);

	//좋아요 상태 확인
	@Query(value = "SELECT COUNT(*) FROM jpa_board_recommend WHERE email = :email AND board_idx = :boardIdx and good_chk = 'Y'", nativeQuery = true)
	int countByEmailAndBoardIdx(@Param("email") String email, @Param("boardIdx") Long boardIdx);

	//신고 상태 확인
	@Query(value =  "SELECT COUNT(*) jpa_board_recommend  WHERE email = :email AND board_idx = :boardIdx AND bad_Chk = 'Y'", nativeQuery = true)
	int countReportsByEmailAndBoardIdx(@Param("email") String email, @Param("boardIdx") Long boardIdx);

}
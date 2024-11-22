package com.kodinghaejo.entity.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kodinghaejo.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

	Page<TestEntity> findByTitleContaining(String searchKeyword, Pageable pageable);
	
	//제목과 사용여부로 문제 리스트 검색
	public List<TestEntity> findByTitleContainingAndIsUse(String title, String isUse);
	
	//난이도 검색필터에 포함되는 문제 리스트
	public List<TestEntity> findByDiff(int diff);
	
	//등록일 기준으로 신규문제 출력
	@Query("SELECT t FROM test t ORDER BY t.regdate DESC")
    List<TestEntity> findNewTest(Pageable pageable);
	
}
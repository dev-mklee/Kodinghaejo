package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.TestQuestionEntity;

public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {
	
	List<TestQuestionEntity> findByTitleContaining(String searchKeyword);
}
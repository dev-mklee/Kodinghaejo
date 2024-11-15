package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionEntity;

public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {

	List<TestQuestionEntity> findByTitleContaining(String searchKeyword);
	
}
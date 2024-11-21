package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;

public interface TestQuestionAnswerRepository extends JpaRepository<TestQuestionAnswerEntity, Long> {

	List<TestQuestionAnswerEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}
package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestSubmitEntity;

public interface TestSubmitRepository extends JpaRepository<TestSubmitEntity, Long> {
	
	public long countByRegdateBetween(LocalDateTime start, LocalDateTime end);
}
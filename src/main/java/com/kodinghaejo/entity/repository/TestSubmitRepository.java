package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestSubmitEntity;

public interface TestSubmitRepository extends JpaRepository<TestSubmitEntity, Long> {
	
	public long countByRegdateBetween(LocalDateTime start, LocalDateTime end);
	
	
	@Query("SELECT COUNT(t) FROM testSubmit t WHERE t.tlIdx.testIdx.idx = :testIdx")
    long countByTestIdx(@Param("testIdx") Long testIdx);
	
	@Query("SELECT COUNT(t) FROM testSubmit t WHERE t.tlIdx.testIdx.idx = :testIdx AND t.submSts = :submSts")
	long countByTestIdxAndSubmSts(@Param("testIdx") Long testIdx,@Param("submSts") String submSts);
}
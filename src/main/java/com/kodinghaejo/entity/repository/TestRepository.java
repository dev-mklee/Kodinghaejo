package com.kodinghaejo.entity.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
	
	Page<TestEntity> findByTitleContaining(String searchKeyword, Pageable pageable);

}
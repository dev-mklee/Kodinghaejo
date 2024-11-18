package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.CommonCodeEntity;

import jakarta.transaction.Transactional;

public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, String> {

	public List<CommonCodeEntity> findByIsUse(String isUse);
	
	public List<CommonCodeEntity> findByCodeContaining(String SearchKeyword);
	
	List<CommonCodeEntity> findByType(String type);
	
	
	@Modifying
	@Transactional
	@Query("DELETE FROM commonCode c WHERE c.code = :code")
	public int deleteByCode(@Param("code") String code);
}
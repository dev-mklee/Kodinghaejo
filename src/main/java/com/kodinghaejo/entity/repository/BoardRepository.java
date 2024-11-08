package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	public List<BoardEntity> findByIsUseOrderByRegdateDesc(String isUse);
	
	List<BoardEntity> findByCatNot(String cat);
	
	List<BoardEntity> findByCat(String cat);
}
package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.BoardRecommendEntityId;

public interface BoardRecommendRepository extends JpaRepository<BoardRecommendEntity, BoardRecommendEntityId> {

}
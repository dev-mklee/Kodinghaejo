package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.MemberLogEntity;
import com.kodinghaejo.entity.MemberLogEntityId;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, MemberLogEntityId> {
	
}
package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.ChatMsgEntity;
import com.kodinghaejo.entity.MemberEntity;

public interface ChatMsgRepository extends JpaRepository<ChatMsgEntity, Long> {
	
}
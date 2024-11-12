package com.kodinghaejo.entity.repository;


import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.kodinghaejo.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

	public Optional<MemberEntity> findByEmailAndIsUse(String email, String isUse);
	public Optional<MemberEntity> findByUsernameAndTelAndIsUse(String username, String tel, String isUse);

	
    List<MemberEntity> findByEmailContaining(String email);
    List<MemberEntity> findByNicknameContaining(String nickname);
    List<MemberEntity> findByUsernameContaining(String username);
    
    public long countByRegdateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    

}
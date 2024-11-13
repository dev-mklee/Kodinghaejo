package com.kodinghaejo.entity.repository;


import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.kodinghaejo.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

	public Optional<MemberEntity> findByEmailAndIsUse(String email, String isUse);
	public Optional<MemberEntity> findByUsernameAndTelAndIsUse(String username, String tel, String isUse);

	//이메일,닉네임,이름 별 검색
    public List<MemberEntity> findByEmailContaining(String email);
    public List<MemberEntity> findByNicknameContaining(String nickname);
    public List<MemberEntity> findByUsernameContaining(String username);
    
    //일별 가입자 수
    public long countByRegdateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    

}
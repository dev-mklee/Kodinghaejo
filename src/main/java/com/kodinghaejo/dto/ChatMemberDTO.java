package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMemberDTO {

	private Long chatIdx;
	private MemberEntity email;
	private String nickname;
	private String manager;
	private LocalDateTime regdate;
	private String isUse;
	
	//Entity --> DTO 이동
	public ChatMemberDTO(ChatMemberEntity entity) {
		this.chatIdx = entity.getChatIdx().getIdx();
		this.email = entity.getEmail();
		this.nickname = entity.getNickname();
		this.manager = entity.getManager();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}
	
	//DTO --> Entity 이동
	public ChatMemberEntity dtoToEntity(ChatMemberDTO dto, ChatEntity chatEntity) {
		ChatMemberEntity entity = ChatMemberEntity
									.builder()
									.chatIdx(chatEntity)
									.email(dto.getEmail())
									.nickname(dto.getNickname())
									.manager(dto.getManager())
									.regdate(dto.getRegdate())
									.isUse(dto.getIsUse())
									.build();
		
		return entity;
	}
	
}
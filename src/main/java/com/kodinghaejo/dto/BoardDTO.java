package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.BoardEntity;
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
public class BoardDTO {

	private Long idx;
	private String bCat;
	private MemberEntity email;
	private String writer;
	private String title;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public BoardDTO(BoardEntity entity) {
		this.idx = entity.getIdx();
		this.bCat = entity.getBCat();
		this.email = entity.getEmail();
		this.writer = entity.getWriter();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public BoardEntity dtoToEntity(BoardDTO dto) {
		BoardEntity entity = BoardEntity
								.builder()
								.idx(dto.getIdx())
								.bCat(dto.getBCat())
								.email(dto.getEmail())
								.writer(dto.getWriter())
								.title(dto.getTitle())
								.content(dto.getContent())
								.regdate(dto.getRegdate())
								.isUse(dto.getIsUse())
								.build();

		return entity;
	}

}
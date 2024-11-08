package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;

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
public class TestLngDTO {

	private Long idx;
	private TestEntity testIdx;
	private String lng;
	private String content;
	private String correct;
	private String mainSrc;
	private String runSrc;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public TestLngDTO(TestLngEntity entity) {
		this.idx = entity.getIdx();
		this.testIdx = entity.getTestIdx();
		this.lng = entity.getLng();
		this.content = entity.getContent();
		this.correct = entity.getCorrect();
		this.mainSrc = entity.getMainSrc();
		this.runSrc = entity.getRunSrc();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestLngEntity dtoToEntity(TestLngDTO dto) {
		TestLngEntity entity = TestLngEntity
									.builder()
									.idx(dto.getIdx())
									.testIdx(dto.getTestIdx())
									.lng(dto.getLng())
									.content(dto.getContent())
									.correct(dto.getCorrect())
									.mainSrc(dto.getMainSrc())
									.runSrc(dto.getRunSrc())
									.regdate(dto.getRegdate())
									.isUse(dto.getIsUse())
									.build();

		return entity;
	}

}
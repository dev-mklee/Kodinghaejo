package com.kodinghaejo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kodinghaejo.entity.TestEntity;

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
public class TestDTO {

	private Long idx;
	private String title;
	private int diff;
	private String descr;
	private LocalDateTime regdate;
	private String isUse;

	private List<TestLngDTO> testLngList; //문제 - 포함 언어 목록
	private String submSts; //문제 - 상태
	private long submitCount; //문제 - 제출 인원 수
	private long correctCount; //문제 - 완료 인원 수
	private double correctRate; //문제 - 정답률

	//Entity --> DTO 이동
	public TestDTO(TestEntity entity) {
		this.idx = entity.getIdx();
		this.title = entity.getTitle();
		this.diff = entity.getDiff();
		this.descr = entity.getDescr();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestEntity dtoToEntity(TestDTO dto) {
		TestEntity entity = TestEntity
													.builder()
													.idx(dto.getIdx())
													.title(dto.getTitle())
													.diff(dto.getDiff())
													.descr(dto.getDescr())
													.regdate(dto.getRegdate())
													.isUse(dto.getIsUse())
													.build();

		return entity;
	}

}
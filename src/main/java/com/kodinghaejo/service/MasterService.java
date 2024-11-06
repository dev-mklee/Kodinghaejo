package com.kodinghaejo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.TestEntity;


@Service
public interface MasterService {
	
	//문제 작성
	void saveTestWrite(TestDTO testDTO);
	
	//문제 보여주기
	public List<TestEntity> testAllList();
	
}
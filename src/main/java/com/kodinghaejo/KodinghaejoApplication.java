package com.kodinghaejo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KodinghaejoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KodinghaejoApplication.class, args);
	}

}
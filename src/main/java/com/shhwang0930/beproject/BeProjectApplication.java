package com.shhwang0930.beproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeProjectApplication.class, args);
	}

}

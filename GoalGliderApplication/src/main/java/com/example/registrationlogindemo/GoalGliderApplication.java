package com.example.registrationlogindemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GoalGliderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoalGliderApplication.class, args);
	}

}

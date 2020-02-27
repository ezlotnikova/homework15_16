package com.gmail.ezlotnikova.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.gmail.ezlotnikova.springboot",
		"com.gmail.ezlotnikova.service",
		"com.gmail.ezlotnikova.repository"})
public class SpringBootModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootModuleApplication.class, args);
	}

}

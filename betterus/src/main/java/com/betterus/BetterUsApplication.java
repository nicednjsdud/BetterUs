package com.betterus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BetterUsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetterUsApplication.class, args);
	}

}

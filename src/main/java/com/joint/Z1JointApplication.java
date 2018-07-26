package com.joint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Z1JointApplication {

	public static void main(String[] args) {
		SpringApplication.run(Z1JointApplication.class, args);
	}
}

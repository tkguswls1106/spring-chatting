package com.shj.springchatting;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.TimeZone;

// @EnableJpaAuditing
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})  // 일단 DB 미적용.
public class SpringChattingApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringChattingApplication.class, args);
	}

}

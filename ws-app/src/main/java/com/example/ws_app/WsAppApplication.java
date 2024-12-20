package com.example.ws_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsAppApplication.class, args);
	}

}

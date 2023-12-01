package com.example.weatherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan("com.example.weatherservice.models")
@SpringBootApplication
@EnableScheduling
public class WeatherserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherserviceApplication.class, args);
	}

}

package com.plazoleta.foodcourtmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.plazoleta.foodcourtmicroservice.application.client.handler")
public class FoodcourtMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodcourtMicroserviceApplication.class, args);
	}

}

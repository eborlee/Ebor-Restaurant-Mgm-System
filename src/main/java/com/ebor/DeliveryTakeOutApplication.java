package com.ebor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j // log provided by lombok
@SpringBootApplication
public class DeliveryTakeOutApplication {

	public static void main(String[] args) {


		SpringApplication.run(DeliveryTakeOutApplication.class, args);
		log.info("====== Successfully start the project ======");
	}

}

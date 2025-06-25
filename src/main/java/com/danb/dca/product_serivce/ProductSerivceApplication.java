package com.danb.dca.product_serivce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ProductSerivceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductSerivceApplication.class, args);
	}

}

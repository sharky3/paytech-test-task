package com.magdieva.testtask;

import com.magdieva.testtask.config.PaymentProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PaymentProperties.class)
public class PaytechClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaytechClientApplication.class, args);
	}

}
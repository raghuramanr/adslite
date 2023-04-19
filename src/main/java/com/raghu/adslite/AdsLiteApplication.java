package com.raghu.adslite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class AdsLiteApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AdsLiteApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8000"));
		app.run(args);
	}

}

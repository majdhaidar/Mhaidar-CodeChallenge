package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BltAppApplication {

	public static void main(String[] args) {
		//Enable AWT for web server application
		//SpringApplication.run(BltAppApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(BltAppApplication.class);
	    builder.headless(false)
	           // .web(WebApplicationType.NONE)
	           // .bannerMode(Banner.Mode.OFF)
	            .run(args);
	}

}

package org.sid;

import org.sid.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class) // class we've created before in the package config
public class SpringRedditCloneApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(SpringRedditCloneApplication.class, args);
	}
}

package com.tony.health_jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableScheduling
public class HealthJobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthJobsApplication.class, args);
    }

}

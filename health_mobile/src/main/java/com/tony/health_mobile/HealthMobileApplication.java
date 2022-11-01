package com.tony.health_mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class HealthMobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthMobileApplication.class, args);
    }

}

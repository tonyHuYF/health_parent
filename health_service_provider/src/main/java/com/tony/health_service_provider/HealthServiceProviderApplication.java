package com.tony.health_service_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class HealthServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthServiceProviderApplication.class, args);
    }

}

package com.develop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class authApplication {
    public static void main(String[] args) {
        SpringApplication.run(authApplication.class, args);
    }
}
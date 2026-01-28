package com.project.hyundai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.project.hyundai")
public class HyundaiStarter {
    public static void main(String[] args) {
        SpringApplication.run(HyundaiStarter.class, args);
    }
}
package com.project.kcredit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.project.kcredit")
public class DemoStarter {
    public static void main(String[] args) {
        SpringApplication.run(DemoStarter.class, args);
    }
}
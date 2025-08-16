package com.zazhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZZCoderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZZCoderApplication.class, args);
    }
}

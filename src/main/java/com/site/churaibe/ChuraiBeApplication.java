package com.site.churaibe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChuraiBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChuraiBeApplication.class, args);
    }
}
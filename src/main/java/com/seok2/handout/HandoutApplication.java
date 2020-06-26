package com.seok2.handout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HandoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandoutApplication.class, args);
    }

}

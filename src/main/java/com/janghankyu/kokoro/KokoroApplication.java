package com.janghankyu.kokoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KokoroApplication {

    public static void main(String[] args) {
        SpringApplication.run(KokoroApplication.class, args);
    }

}

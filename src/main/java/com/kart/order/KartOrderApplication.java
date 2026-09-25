package com.kart.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KartOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KartOrderApplication.class, args);
    }

}

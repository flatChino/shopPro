package com.example.shoppro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication

public class ShopProApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopProApplication.class, args);
    }

}

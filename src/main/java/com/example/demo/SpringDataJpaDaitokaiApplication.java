package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.demo.persistence.entity",
        "org.springframework.data.jpa.convert.threeten"})
public class SpringDataJpaDaitokaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaDaitokaiApplication.class, args);
    }
}

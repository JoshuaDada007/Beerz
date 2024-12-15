package com.itec3860.ipaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IpaApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(IpaApiApplication.class, args);

    }

}

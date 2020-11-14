package com.zxb.redistemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zxb"})
public class RedistemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedistemplateApplication.class, args);
    }

}

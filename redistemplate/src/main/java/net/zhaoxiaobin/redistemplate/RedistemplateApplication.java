package net.zhaoxiaobin.redistemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.zhaoxiaobin"})
public class RedistemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedistemplateApplication.class, args);
    }

}

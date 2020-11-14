package com.zxb.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class RedisSessionApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisSessionApplication.class, args);
    }
}

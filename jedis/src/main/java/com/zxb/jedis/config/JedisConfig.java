/**
 * Copyright (C), 2015-2020
 */
package com.zxb.jedis.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author zhaoxb
 * @create 2020-05-05 17:23
 */
@Configuration
@Slf4j
public class JedisConfig {
    @Bean
    public JedisPoolConfig jedisPoolConfig(@Value("${jedis.maxTotal}") int maxActive,
                                           @Value("${jedis.maxIdle}") int maxIdle,
                                           @Value("${jedis.minIdle}") int minIdle,
                                           @Value("${jedis.maxWaitMillis}") long maxWaitMillis,
                                           @Value("${jedis.testOnBorrow}") boolean testOnBorrow) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool(@Value("${jedis.host}") String host,
                               @Value("${jedis.password}") String password,
                               @Value("${jedis.port}") int port,
                               @Value("${jedis.timeout}") int timeout, JedisPoolConfig jedisPoolConfig) {

        log.info("=====创建JedisPool连接池=====");
        if(StringUtils.isNotEmpty(password)) {
            return new JedisPool(jedisPoolConfig, host, port, timeout, password);
        }

        return new JedisPool(jedisPoolConfig, host, port, timeout);
    }

    @Bean
    public JedisCluster jedisCluster(JedisPoolConfig jedisPoolConfig) {
        Set<HostAndPort> nodeSet = new HashSet<>();
        nodeSet.add(new HostAndPort("172.16.122.101", 6379));
        nodeSet.add(new HostAndPort("172.16.122.101", 6380));
        nodeSet.add(new HostAndPort("172.16.122.102", 6379));
        nodeSet.add(new HostAndPort("172.16.122.102", 6380));
        nodeSet.add(new HostAndPort("172.16.122.103", 6379));
        nodeSet.add(new HostAndPort("172.16.122.103", 6380));
        JedisCluster jedisCluster = new JedisCluster(nodeSet, 2000, jedisPoolConfig);

        log.info("=====创建JedisCluster=====");

        return jedisCluster;
    }
}
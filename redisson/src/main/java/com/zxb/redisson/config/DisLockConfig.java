package com.zxb.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaoxb
 * @date 2020/08/25 3:06 下午
 */
@Configuration
public class DisLockConfig {
    @Autowired
    private RedisProperties redisProperties;

    /**
     * Cluster集群模式构建 RedissonClient
     *
     * @return
     */
    @Bean
    public RedissonClient clusterRedissonClient() {
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .setPassword(redisProperties.getPassword())
                .setScanInterval(5000);

        // 注册集群各个节点
        for (String node : redisProperties.getCluster().getNodes()) {
            clusterServersConfig.addNodeAddress("redis://".concat(node));
        }
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }


    /**
     * 单机模式构建 RedissonClient
     *
     * @return
     */
//    @Bean
    public RedissonClient singleRedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://ip:port")
                .setPassword("password")
                .setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

    /**
     * 哨兵模式构建 RedissonClient
     *
     * @return
     */
//    @Bean
    public RedissonClient sentinelRedissonClient() {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("redis://ip1:port1",
                "redis://ip2:port2",
                "redis://ip3:port3")
                .setMasterName("mymaster")
                .setPassword("password")
                .setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
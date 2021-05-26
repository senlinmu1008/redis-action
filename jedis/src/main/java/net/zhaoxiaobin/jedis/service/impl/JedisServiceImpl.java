/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.jedis.service.impl;

import net.zhaoxiaobin.jedis.service.JedisService;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author zhaoxb
 * @create 2020-05-05 16:30
 */
@Service("jedisService")
public class JedisServiceImpl implements JedisService {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public void set(String key, String value) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
    }

    @Override
    public String get(String key) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.get(key);
    }

    @Override
    public boolean exists(String key) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.exists(key);
    }

    @Override
    public long expire(String key, int seconds) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.expire(key ,seconds);
    }

    /**
     * 返回key值剩余时间，单位：秒
     * -1：代表永久，-2：代表已过期或不存在
     */
    @Override
    public long ttl(String key) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.ttl(key);
    }

    @Override
    public long incr(String key) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.incr(key);
    }

    @Override
    public long decr(String key) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.decr(key);
    }

    @Override
    public long del(String key) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.del(key);
    }
}
/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.jedis.service;

/**
 *
 * @author zhaoxb
 * @create 2020-05-05 16:30
 */
public interface JedisService {
    void set(String key, String value);
    String get(String key);
    boolean exists(String key);
    long expire(String key, int seconds);
    long ttl(String key);
    long incr(String key);
    long decr(String key);
    long del(String key);
}

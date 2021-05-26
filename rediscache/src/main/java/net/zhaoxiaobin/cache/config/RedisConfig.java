/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhaoxb
 * @create 2020-05-06 22:07
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * 使用缓存时如果不指定key的策略，则使用自定义策略生成缓存的key
     *
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName().concat("::"));
            sb.append(method.getName().concat("::"));
            for (Object param : params) {
                sb.append(param.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 缓存管理器，适合2.x版本
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 对于指定的cacheNames设置缓存的过期时长
        Map<String, RedisCacheConfiguration> cacheConfigMap = new HashMap<>();
        cacheConfigMap.put("users", this.getCacheConfigurationWithTtl(2 * 60 * 60));

        // 构建CacheManager对象，指定的cacheNames过期时长为2小时，其它默认为1小时
        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigMap)
                .cacheDefaults(this.getCacheConfigurationWithTtl(1 * 60 * 60))
                .build();
    }

    /**
     * 设置缓存过期时长
     *
     * @param seconds 过期时长
     * @return
     */
    private RedisCacheConfiguration getCacheConfigurationWithTtl(long seconds) {
        // 序列化设置
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会抛出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // 2.1.x
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL); // 2.2.x
        jacksonSeial.setObjectMapper(om);

        // 基本配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSeial))
                // 不缓存null
                .disableCachingNullValues()
                // 缓存数据保存时长
                .entryTtl(Duration.ofSeconds(seconds));
        return redisCacheConfiguration;
    }
}
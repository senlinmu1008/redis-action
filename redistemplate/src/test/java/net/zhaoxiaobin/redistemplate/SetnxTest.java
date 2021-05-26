package net.zhaoxiaobin.redistemplate;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author zhaoxb
 * @date 2020/08/13 10:06 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SetnxTest {
    @Autowired
    private ValueOperations<String, String> stringOperations;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testSetnx() {
        IntStream.range(0, 5).parallel().forEach(i -> {
            String uuid = IdUtil.randomUUID();
            Boolean lock = stringOperations.setIfAbsent("lock", uuid, 10, TimeUnit.SECONDS);
            log.info("是否获取锁:[{}]", lock);
            if (!lock) return;
            // 获得锁后的操作
            // ...

            // 最后释放锁
            redisTemplate.delete("lock");
        });
    }
}
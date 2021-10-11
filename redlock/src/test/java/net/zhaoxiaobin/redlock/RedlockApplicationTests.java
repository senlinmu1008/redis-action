package net.zhaoxiaobin.redlock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
public class RedlockApplicationTests {

    @Test
    public void testDisLock() {
        // 配置多台互相独立的redis单机节点
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://172.16.122.104:6379").setDatabase(0);
        Config config2 = new Config();
        config2.useSingleServer().setAddress("redis://172.16.122.104:6380").setDatabase(0);
        Config config3 = new Config();
        config3.useSingleServer().setAddress("redis://172.16.122.104:6381").setDatabase(0);

        // 构建RedissonClient
        RedissonClient redissonClient1 = Redisson.create(config1);
        RedissonClient redissonClient2 = Redisson.create(config2);
        RedissonClient redissonClient3 = Redisson.create(config3);

        // 5个线程并发去获取锁
        IntStream.range(0, 5).parallel().forEach(i -> tryRedlock(redissonClient1, redissonClient2, redissonClient3));
    }

    @SneakyThrows
    private void tryRedlock(RedissonClient... redissonClients) {
        // 构建Redlock对象
        RLock[] rLock = new RLock[redissonClients.length];
        for (int i = 0; i < rLock.length; i++) {
            rLock[i] = redissonClients[i].getLock("redlock");
        }
        RLock redLock = new RedissonRedLock(rLock);

        // 基于Redlock对象去操作，与redisson实现普通的分布式锁一样
        // 获取锁最多等待500ms，10s后key过期自动释放锁
        boolean tryLock = redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
        if (tryLock) {
            // 获取到锁后开始执行对资源的操作
            try {
                log.info("当前线程:[{}]获得锁", Thread.currentThread().getName());
                // 操作资源...
            } finally {
                redLock.unlock();
            }
        } else {
            log.info("当前线程:[{}]没有获得锁", Thread.currentThread().getName());
        }
    }
}

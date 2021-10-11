package net.zhaoxiaobin.redisson;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedissonApplicationTests {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testDisLock() {
        // 5个线程并发去获取锁
//        IntStream.range(0, 5).parallel().forEach(i -> tryLock());
        IntStream.range(0, 5).parallel().forEach(i -> lock());
    }

    /**
     * 锁自动过期
     */
    @SneakyThrows
    private void tryLock() {
        RLock disLock = redissonClient.getLock("disLock");
        // 获取锁最多等待500ms，10s后key过期自动释放锁
        boolean tryLock = disLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
        if (tryLock) {
            // 获取到锁后开始执行对资源的操作
            try {
                log.info("当前线程:[{}]获得锁", Thread.currentThread().getName());
                // 操作资源...
            } finally {
                disLock.unlock();
            }
        } else {
            log.info("当前线程:[{}]没有获得锁", Thread.currentThread().getName());
        }
    }

    /**
     * watch dog看门狗自动续期锁
     */
    @SneakyThrows
    private void lock() {
        RLock disLock = redissonClient.getLock("disLock");
        // 获取锁最多等待500ms，这里不要显示指定锁过期时间
        // 默认30秒后自动过期，每隔30/3=10秒，看门狗（守护线程）会去续期锁，重设为30秒
        boolean tryLock = disLock.tryLock(500, TimeUnit.MILLISECONDS);
        if (tryLock) {
            // 获取到锁后开始执行对资源的操作
            try {
                log.info("当前线程:[{}]获得锁", Thread.currentThread().getName());
                // 操作资源...
            } finally {
                disLock.unlock();
            }
        } else {
            log.info("当前线程:[{}]没有获得锁", Thread.currentThread().getName());
        }
    }


    @Test
    @SneakyThrows
    public void testTryLockAgain() {
        RLock disLock = redissonClient.getLock("disLock");
        // 获取锁最多等待500ms，10s后key过期自动释放锁
        boolean tryLock = disLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
        if (tryLock) {
            try {
                log.info("当前线程:[{}]获得锁,持有锁次数:[{}]", Thread.currentThread().getName(), disLock.getHoldCount());
                // 操作资源...

                // 测试可重入，锁过期时间会重新计时
                boolean tryLockAgain = disLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
                log.info("当前线程:[{}]是否再次拿到锁:[{}],持有锁次数:[{}]", Thread.currentThread().getName(), tryLockAgain, disLock.getHoldCount());
                // 再次操作资源...
            } finally {
                disLock.forceUnlock();
                log.info("当前线程是否持有锁:[{}],持有锁次数:[{}]", disLock.isHeldByCurrentThread(), disLock.getHoldCount());
            }
        } else {
            log.info("当前线程:[{}]没有获得锁", Thread.currentThread().getName());
        }
    }
}

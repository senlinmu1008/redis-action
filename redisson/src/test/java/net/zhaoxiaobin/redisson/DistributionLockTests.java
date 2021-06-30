package net.zhaoxiaobin.redisson;

import lombok.extern.slf4j.Slf4j;
import net.zhaoxiaobin.redisson.service.DistributionLockDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

/**
 * @author zhaoxb
 * @date 2021-06-28 1:17 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DistributionLockTests {
    @Autowired
    private DistributionLockDemo distributionLockDemo;

    /**
     * 多线程并行处理50个请求，扣库存
     */
    @Test
    public void testDistributionLock() {
        // 粗粒度锁，锁的key固定为方法注解上指定的名称
        long successCount = IntStream.range(0, 50).parallel().filter(j -> distributionLockDemo.increment()).count();

        // 细粒度锁，锁的key为第一个参数，动态变化
//        long successCount = IntStream.range(0, 50).parallel().filter(j -> distributionLockDemo.increment(j)).count();

        log.info("成功数:{}", successCount);
        log.info("剩余库存:{}", distributionLockDemo.get());
    }
}
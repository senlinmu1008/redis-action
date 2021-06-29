package net.zhaoxiaobin.redisson.service;

import net.zhaoxiaobin.redisson.annotation.DistributionLock;
import org.springframework.stereotype.Service;

/**
 * 测试分布式锁
 *
 * @author zhaoxb
 * @date 2021-06-28 1:14 下午
 */
@Service
public class DistributionLockDemo {
    /**
     * 库存
     */
    public static int count = 100;

    @DistributionLock(value = "incrementLock", waitTime = 1000)
    public boolean increment() {
        if (count > 0) {
            count--;
            return true;
        }
        return false;
    }

    @DistributionLock(index = 0, waitTime = 1000)
    public boolean increment(int i) {
        if (count > 0) {
            count--;
            return true;
        }
        return false;
    }

    public int get() {
        return count;
    }
}

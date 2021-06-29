package net.zhaoxiaobin.redisson.aspect;

import lombok.extern.slf4j.Slf4j;
import net.zhaoxiaobin.redisson.annotation.DistributionLock;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhaoxb
 * @date 2021-06-27 6:55 下午
 */
@Order(Integer.MIN_VALUE + 1)
@Aspect
@Component
@Slf4j
public class DistributionLockAspect {
    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(distributionLock)")
    public Object doAround(ProceedingJoinPoint point, DistributionLock distributionLock) throws Throwable {
        String methodName = point.getSignature().getName();
        if (StringUtils.isNotBlank(distributionLock.value())) {
            // 锁粒度较粗，由目标方法上的DistributionLock注解中指定锁的名称，由同一个业务共享该锁
            return this.tryLock(point, distributionLock.value(), distributionLock.waitTime());
        } else if (distributionLock.index() >= 0){
            // 锁粒度较细，由目标方法的第x个参数作为锁名称，可以是一个业务上的编号、名称等等
            Object[] args = point.getArgs(); // 参数列表
            int index = distributionLock.index(); // 参数列表中第几个参数作为分布式锁的key
            if (args.length <= index) {
                log.error("目标方法:{}上没有第:{}参数", methodName, index);
                throw new RuntimeException("目标方法:" + methodName + "上没有第:" + index + "参数");
            }
            String key = args[index].toString();
            return this.tryLock(point, key, distributionLock.waitTime());
        } else {
            log.error("没有配置具体分布式锁的key,目标方法:{}", methodName);
            throw new RuntimeException("没有配置具体分布式锁的key,目标方法:" + methodName);
        }
    }

    /**
     * 只获取、释放锁，不处理任何异常，原样抛出异常
     *
     * @param point    切入点
     * @param key      分布式锁的key
     * @param waitTime 获取锁等待时间
     * @return
     * @throws Throwable
     */
    private Object tryLock(ProceedingJoinPoint point, String key, int waitTime) throws Throwable {
        RLock disLock = redissonClient.getLock(key);
        try {
            // 默认30秒后自动过期，每隔30/3=10秒，看门狗（守护线程）会去续期锁，重设为30秒
            boolean tryLock = disLock.tryLock(waitTime, TimeUnit.MILLISECONDS);
            if (!tryLock) {
                log.error("获取分布式锁:{}失败", key);
                // 由具体业务决定是抛异常还是返回null或其他业务对象
//                throw new RuntimeException("获取分布式锁失败");
                return false;
            }
            return point.proceed(point.getArgs());
        } finally {
            // 只有获取到锁的线程才执行释放锁操作
            if (disLock.isHeldByCurrentThread()) {
                disLock.unlock();
            }
        }
    }
}
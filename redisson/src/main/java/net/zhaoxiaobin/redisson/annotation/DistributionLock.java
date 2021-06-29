package net.zhaoxiaobin.redisson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhaoxb
 * @date 2021-06-27 6:34 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributionLock {
    /**
     * 分布式锁key
     */
    String value() default "";

    /**
     * 获取分布式锁的等待时间
     */
    int waitTime() default 5 * 1000;

    /**
     * 分布式锁key所在参数列表中的位置
     */
    int index() default -1;
}

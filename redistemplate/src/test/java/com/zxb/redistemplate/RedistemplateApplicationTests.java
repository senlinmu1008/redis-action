package com.zxb.redistemplate;

import cn.hutool.json.JSONUtil;
import com.zxb.redistemplate.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedistemplateApplicationTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, String> stringOperations;

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Test
    public void testString() {
        String key = "name";
        // set 并设置过期时间
        stringOperations.set(key, "李白", 1, TimeUnit.SECONDS);
        // get
        log.info(stringOperations.get(key));
        // 查询key剩余时间SECONDS
        log.info(redisTemplate.getExpire(key, TimeUnit.MILLISECONDS) + "");
        // 删除key
        redisTemplate.delete(key);
        // 判断key是否存在
        log.info(redisTemplate.hasKey(key) + "");
    }

    /**
     * 序列化POJO和反序列化
     */
    @Test
    public void testPOJO() {
        String key = "libai";
        User user = new User();
        user.setId(1L);
        user.setUsername("李白");
        user.setPassword("123456");
        user.setAge(28);
        user.setSex(1);
        valueOperations.set(key, user, 1, TimeUnit.SECONDS);
        User result = (User) valueOperations.get(key);
        log.info(JSONUtil.toJsonPrettyStr(result));
    }

    @Test
    public void testHash() {
        String key = "hashKey";

        // 单个put
        hashOperations.put(key, "name", "李白");
        hashOperations.put(key, "age", 30);
        hashOperations.put(key, "gender", 1);
        // 批量put
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("address", "长安");
        myMap.put("salary", 2000.5);
        hashOperations.putAll(key, myMap);
        // 指定过期时间
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);

        // get指定散列的单个key
        log.info(hashOperations.get(key, "name") + "");
        // 删除指定散列中的某些key
        hashOperations.delete(key, "name", "salary");
        // 判断指定散列是否包含某个key
        log.info(hashOperations.hasKey(key, "salary") + "");

        // 获取指定的整个散列
        Map<String, Object> map = hashOperations.entries(key);
        log.info(JSONUtil.toJsonPrettyStr(map));

        // 获取散列所有的key集合
        Set<String> keySet = hashOperations.keys(key);
        log.info(JSONUtil.toJsonPrettyStr(keySet));

        // 获取散列键值对数
        Long size = hashOperations.size(key);
        log.info(size + "");

        // 获取散列所有的value列表
        List<Object> values = hashOperations.values(key);
        log.info(JSONUtil.toJsonPrettyStr(values));
    }

    @Test
    public void testlist() {
        String key = "listKey";

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("aa");
        user1.setPassword("123456");
        user1.setAge(28);
        user1.setSex(1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("bb");
        user2.setPassword("123456");
        user2.setAge(55);
        user2.setSex(2);
        listOperations.rightPush(key, user1);
        listOperations.rightPush(key, user2);
        // 指定过期时间
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);

        // 遍历列表
        for (int i = 0; i < listOperations.size(key); i++) {
            User user = (User) listOperations.index(key, i); // 根据下标访问列表中的元素
//            User user = (User) listOperations.leftPop(key); // 出队
            log.info(JSONUtil.toJsonPrettyStr(user));
        }

        // 获取列表指定范围
//        List<User> userList = (List) listOperations.range(key, 0, listOperations.size(key));
        // 获取所有元素
        List<User> userList = (List) listOperations.range(key, 0, -1);
        log.info(JSONUtil.toJsonPrettyStr(userList));
    }

    /**
     * 不可重复集合
     */
    @Test
    public void testSet() {
        String key = "setKey";

        Long num = setOperations.add(key, "a", "b", "c", "b");
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        log.info("存储数量:{}", num); // 3

        // 判断是否成员
        log.info(setOperations.isMember(key, "a") + "");

        // 获取整个无序集合
        Set<Object> members = setOperations.members(key);
        log.info(JSONUtil.toJsonPrettyStr(members));

    }

    /**
     * 集合每个value需要关联1个double类型的数字
     */
    @Test
    public void testZset() {
        // 批量添加
        ZSetOperations.TypedTuple<Object> tuple1 = new DefaultTypedTuple<>("libai", 1.1);
        ZSetOperations.TypedTuple<Object> tuple2 = new DefaultTypedTuple<>("zhaoxb", 14.8);
        ZSetOperations.TypedTuple<Object> tuple3 = new DefaultTypedTuple<>("cc", -3.2);
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.add(tuple1);
        set.add(tuple2);
        set.add(tuple3);
        zSetOperations.add("zset", set);

        // 单个添加
        zSetOperations.add("zset", "jj", 0.7);
        zSetOperations.add("zset", "kk", 3.5);
        log.info("zset:{}", zSetOperations.range("zset", 0, -1));

        // 获取元素排名，从0开始
        Long rank = zSetOperations.rank("zset", "zhaoxb");
        log.info("zhaoxb排名:[{}]", rank);

        // 根据分数值大小统计范围（包含头尾）
        Set<Object> rangeByScore = zSetOperations.rangeByScore("zset", 0, 10);
        Long rangeSize = zSetOperations.count("zset", 0, 10);
        log.info("截取[0,10]范围内元素zset:{},共有:[{}]", rangeByScore, rangeSize);

        // 获取指定元素的score
        Double score1 = zSetOperations.score("zset", "zhaoxb");
        Double score2 = zSetOperations.score("zset", "zxb");
        log.info("存在元素的score:[{}],不存在元素的score:[{}]", score1, score2);

        // 删除指定元素
        zSetOperations.remove("zset", "jj", "kk");
        log.info("删除指定元素后,zset:{}", zSetOperations.range("zset", 0, -1));
        // 删除score在某一范围内的元素（包含头尾）
        zSetOperations.removeRangeByScore("zset", -3.2, 1.1);
        log.info("删除score在某一范围内的元素,zset:{}", zSetOperations.range("zset", 0, -1));
        // 删除指定排序范围内的元素
        zSetOperations.removeRange("zset", 0, -1);
        log.info("删除指定排序范围内的元素,zset:{}", zSetOperations.range("zset", 0, -1));
    }

    /**
     * 测试lettuce单个连接实例是否线程安全以及对于多线程并发的压力测试
     */
//    @Test
    public void test() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "50");
        LongStream.range(Long.MIN_VALUE, Long.MAX_VALUE).parallel().forEach(i -> {
            String value = UUID.randomUUID().toString();
            stringOperations.set(i + "", value, 10, TimeUnit.SECONDS);
            String result = stringOperations.get(i + "");
            if (!value.equals(result)) {
                System.err.println("===========");
                System.exit(1);
            }
        });
    }

}

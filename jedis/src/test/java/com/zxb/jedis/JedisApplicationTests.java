package com.zxb.jedis;

import com.zxb.jedis.service.JedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JedisApplicationTests {
	@Autowired
	private JedisService jedisService; // 单机操作

	@Autowired
	private JedisCluster jedisCluster; // 集群操作

	@Test
	public void TestJedis() {
		// set
		jedisService.set("name", "zxb");
		jedisService.set("age", "28");

		// get
		log.info(jedisService.get("name"));

		// exists
		log.info(jedisService.exists("age") + "");

		// expire
		jedisService.expire("name", 1);

		// ttl
		log.info(jedisService.ttl("name") + "");

		// ++ --
		log.info(jedisService.incr("age") + "");
		log.info(jedisService.decr("age") + "");

		// del
		jedisService.del("age");

	}

	@Test
	public void TestJedisCluster() {
		// jedisCluster
		jedisCluster.set("name", "zxb");
		jedisCluster.set("age", "28");

		// get
		log.info(jedisCluster.get("name"));

		// exists
		log.info(jedisCluster.exists("age") + "");

		// expire
		jedisCluster.expire("name", 1);

		// ttl
		log.info(jedisCluster.ttl("name") + "");

		// ++ --
		log.info(jedisCluster.incr("age") + "");
		log.info(jedisCluster.decr("age") + "");

		// del
		jedisCluster.del("age");

	}

}

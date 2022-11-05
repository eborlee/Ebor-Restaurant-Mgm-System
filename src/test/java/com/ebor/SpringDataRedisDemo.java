package com.ebor;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisDemo {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("city","beijing");

        String city = (String) valueOperations.get("city");

        valueOperations.set("key1","value1", 10l, TimeUnit.SECONDS);

        System.out.println(city);

    }
}

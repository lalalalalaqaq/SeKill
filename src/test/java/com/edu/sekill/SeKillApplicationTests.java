package com.edu.sekill;

import com.edu.sekill.dao.UserDAO;
import com.edu.sekill.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class SeKillApplicationTests {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    UserDAO userDAO;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("myKey","myValue");
        System.out.println(redisTemplate.opsForValue().get("myKey"));
    }

    @Test
    public void testMybatis(){
        User select = userDAO.select(1);
        System.out.println(select);
    }

}

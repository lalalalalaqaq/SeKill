package com.edu.sekill.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-25
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int saveUserCount(Integer userId) {
        //根据用户ID生成key并且放入Redis
        String limitKey="LIMIT_"+userId;
        String limitnum = stringRedisTemplate.opsForValue().get(limitKey);
        int limit=-1;
        if (limitnum==null){
            //第一次调用放入Redis中，并且设置为0 【设置为3分钟，限制访问100次】
            stringRedisTemplate.opsForValue().set(limitKey,String.valueOf(0),60*3, TimeUnit.SECONDS);
            limit=0;
        }else {
            //不是第一次调用，Redis计数器加一
            limit=Integer.parseInt(limitnum)+1;
            stringRedisTemplate.opsForValue().set(limitKey,String.valueOf(limit),60*3,TimeUnit.SECONDS);
        }
        //返回调用次数
        return limit;
    }

    @Override
    public boolean getUserCount(Integer userId) {

        // 根据userid key获取调用次数
        String limitKey="LIMIT_"+userId;
        //从Redis中获取次数
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        if (limitNum==null){
            log.info("该用户没有访问申请的验证值记录，疑似异常");
            throw new RuntimeException("数据缓存以过期，请刷新页面");
        }
        //【设置为3分钟，限制访问100次】【为了测试方便，修改为5次】
        return Integer.parseInt(limitNum)>5;
    }
}

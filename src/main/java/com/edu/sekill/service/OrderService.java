package com.edu.sekill.service;

import org.springframework.stereotype.Service;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-23
 **/

@Service
public interface OrderService {

    // 用来处理秒杀的下单方法 并返回订单id
    int kill(Integer id);

    // 生成md5签名方法
    String getMd5(Integer id, Integer userid);

    // 处理秒杀的下单方法 返回订单id 加入了md5签名 即接口隐藏
    int kill(Integer id, Integer userid, String md5);
}

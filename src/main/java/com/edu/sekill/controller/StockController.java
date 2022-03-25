package com.edu.sekill.controller;

import com.edu.sekill.service.OrderService;
import com.edu.sekill.service.UserService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-23
 **/
@RestController
@RequestMapping("stock")
@Slf4j
public class StockController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // 创建令牌桶
    private RateLimiter limiter = RateLimiter.create(50);


    // TODO MD5
    @RequestMapping("md5")
    public String getMd5(Integer id, Integer userid){
        String md5;
        try{
            md5 = orderService.getMd5(id,userid);
        }catch (Exception e){
            e.printStackTrace();
            return "获取md5失败" + e.getMessage();
        }
        return "获取md5的信息:" + md5;
    }

    // TODO 乐观锁 + 令牌桶
    // 秒杀方法
    @GetMapping("kill")
    public String kill(Integer id,Integer userid,String md5){
        log.info("商品id : {} ",id);
        if(limiter.tryAcquire(5,TimeUnit.SECONDS)){
            log.info("抢购失败");
            return "抢购失败";
        }
        try {
            int count = userService.saveUserCount(userid);
            log.info("用户访问次数 : {}",count);
            boolean isBanned = userService.getUserCount(userid);
            if (isBanned) {
                log.info("购买失败,超频率");
                return "购买失败,超频率";
            }

            int orderId = orderService.kill(id,userid,md5);
            return  "秒杀成功,订单id为" + String.valueOf(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

    }


//    // TODO 乐观锁 + 令牌桶
//    // 秒杀方法
//    @GetMapping("kill")
//    public String kill(Integer id,Integer userid,String md5){
//        log.info("商品id : {} ",id);
//        if(limiter.tryAcquire(1,TimeUnit.SECONDS)){
//            return "抢购失败";
//        }else{
//            try {
//                int orderId = orderService.kill(id,userid,md5);
//                return  "秒杀成功,订单id为" + String.valueOf(orderId);
//            }catch (Exception e){
//                e.printStackTrace();
//                return e.getMessage();
//            }
//        }
//
//
//    }



//    // TODO 乐观锁 + 令牌桶
//    // 秒杀方法
//    @GetMapping("kill")
//    public String kill(Integer id){
//        log.info("商品id : {} ",id);
//        if(limiter.tryAcquire(50,TimeUnit.SECONDS)){
//            log.info("抢购失败 : {}",limiter.tryAcquire(50,TimeUnit.SECONDS));
//            return "抢购失败";
//        }else{
//            try {
//                int orderId = orderService.kill(id);
//                return  "秒杀成功,订单id为" + String.valueOf(orderId);
//            }catch (Exception e){
//                e.printStackTrace();
//                return e.getMessage();
//            }
//        }
//
//
//    }




      // TODO 令牌桶算法
//    @GetMapping("kill")
//    private String kill(Integer id){
////        //1.没有获取到token请求的一直阻塞直到获取token令牌
////        log.info("等待的时间" + limiter.acquire());
////        System.out.println("处理业务----");
//
//        //2.设置一个等待时间，如果在等待时间内获取到了token,则处理业务
//        //  若无法获取 则失败
//        if(limiter.tryAcquire(1, TimeUnit.SECONDS)){
//            System.out.println("限流,无法秒杀");
//            return "抢购失败";
//        }
//        System.out.println("处理业务");
//        return "测试令牌桶";
//    }

      // TODO 乐观锁
//    // 秒杀方法
//    @GetMapping("kill")
//    public String kill(Integer id){
//        // 根据秒杀商品id 去执行秒杀业务
//        System.out.println("id = " + id);
//        try {
//            int orderId = orderService.kill(id);
//            return  "秒杀成功,订单id为" + String.valueOf(orderId);
//        }catch (Exception e){
//            e.printStackTrace();
//            return e.getMessage();
//        }
//
//    }

      // TODO 悲观锁
//    // 秒杀方法
//    @GetMapping("kill")
//    public String kill(Integer id){
//        // 根据秒杀商品id 去执行秒杀业务
//        System.out.println("id = " + id);
//        try {
//            synchronized (this){
//                int orderId = orderService.kill(id);
//                return  "秒杀成功,订单id为" + String.valueOf(orderId);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            return e.getMessage();
//        }
//
//    }



}

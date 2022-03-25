package com.edu.sekill.service;

import com.edu.sekill.dao.OrderDAO;
import com.edu.sekill.dao.StockDAO;
import com.edu.sekill.dao.UserDAO;
import com.edu.sekill.entity.Order;
import com.edu.sekill.entity.Stock;
import com.edu.sekill.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-23
 **/
@Service
@Slf4j
//@Transactional  TODO
public class OrderServiceImpl implements OrderService{

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public int kill(Integer id, Integer userid, String md5) {
        // 验证商品超时
//        if(!stringRedisTemplate.hasKey("kill"+id)){
//            throw new RuntimeException("当前抢购活动已经结束");
//        }

        // 先验证签名
        String hashkey = "KEY_" + userid + "_" + id;

        String s = stringRedisTemplate.opsForValue().get(hashkey);
        if(s == null){
            throw new RuntimeException("没有携带签名验证不合法");
        }
        if (!s.equals(md5)) {
            throw new RuntimeException("当前请求数据不合法, 请稍后再试");
        }

        Stock stock = checkStock(id);
        updateSale(stock);
        return createOrder(stock);
    }

    //  TODO
    @Override
    public int kill(Integer id) {
        if(!stringRedisTemplate.hasKey("kill"+id)){
            throw new RuntimeException("当前抢购活动已经结束");
        }
        Stock stock = checkStock(id);
        updateSale(stock);
        return createOrder(stock);
    }

    @Override
    public String getMd5(Integer id, Integer userid) {
        // 验证用户的合法性 存在用户信息
        // 验证商品合法性 存在商品信息
        // 生成md5签名
        User user = userDAO.findById(userid);
        if(user == null){
            throw new RuntimeException("用户信息不存在");
        }
        log.info("用户信息 : {}",user.toString());

        Stock stock = stockDAO.checkStock(id);
        if(stock==null){
            throw new RuntimeException("用户信息不合法");
        }
        log.info("商品信息 : {}",stock.toString());

        String hashkey = "KEY_" + userid + "_" + id;
        String key = DigestUtils.md5DigestAsHex((userid+id+"!Q*js#").getBytes());
        stringRedisTemplate.opsForValue().set(hashkey,key,60, TimeUnit.SECONDS);
        log.info("Redis写入: {} | {}",hashkey,key);

        return key;
    }

    //检验库存
    private Stock checkStock(Integer id){
        // 根据商品id更新库存
        Stock stock = stockDAO.checkStock(id);
        if(stock.getSale().equals(stock.getCount())){
            throw new RuntimeException("库存不足");
        }
        return stock;

    }

    //扣除库存
    private void updateSale(Stock stock){
        // 在sql层面完成 销量+1 和 版本号+1   并同时更新商品信息
        int updateRows = stockDAO.updateSale(stock);
        if(updateRows == 0){
            throw new RuntimeException("抢购失败,请重试");
        }
    }

    //创建订单
    private Integer createOrder(Stock stock){
        Order order = new Order();
        order.setSid(stock.getId()).setName(stock.getName()).setCreatetime(new Date());
        orderDAO.createOrder(order);
        return order.getId();
    }

}

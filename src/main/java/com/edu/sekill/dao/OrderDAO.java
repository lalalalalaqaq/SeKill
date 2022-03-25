package com.edu.sekill.dao;

import com.edu.sekill.entity.Order;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-23
 **/
public interface OrderDAO {
    //生成订单方法
    void createOrder(Order order);
}

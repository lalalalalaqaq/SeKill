package com.edu.sekill.dao;

import com.edu.sekill.entity.Stock;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-23
 **/
public interface StockDAO {

    //根据商品id查询库存信息的方法
    Stock checkStock(Integer id);

    //根据商品id扣除库存

    int updateSale(Stock stock);



}

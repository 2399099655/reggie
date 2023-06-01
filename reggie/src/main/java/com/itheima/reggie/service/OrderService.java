package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);


    /**
     * 用户查到历史订单记录
     */
    public Page<Orders>  page(int page,int pageSize);


    /**
     * 管理端后台查看订单
     */
    public  Page<Orders> pageM(int page,int pageSize);
}

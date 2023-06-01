package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {




    //添加购物车
    public ShoppingCart  addCart(ShoppingCart shoppingCart);



    //减少购物车
    public  ShoppingCart subCart(ShoppingCart  shoppingCart);



    //查看购物车
    public List<ShoppingCart> list();

}

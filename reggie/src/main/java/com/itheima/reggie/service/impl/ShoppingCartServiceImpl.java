package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private  ShoppingCartService shoppingCartService;




    //添加购物车
    @Override
    public ShoppingCart addCart(ShoppingCart shoppingCart) {
        log.info("购物车数据:{}",shoppingCart);

       Long nowId= BaseContext.getCurrentId();
       shoppingCart.setUserId(nowId);

       Long dishId=shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,nowId);

        if(shoppingCart.getDishId()!=null)
        {
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCart1=shoppingCartService.getOne(lambdaQueryWrapper);

        if (shoppingCart1 != null)
        {
            Integer num = shoppingCart1.getNumber();
            shoppingCart1.setNumber(num+1);
            shoppingCartService.updateById(shoppingCart1);
        }
        else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1=shoppingCart;
        }
        return  shoppingCart1;
    }



    //减少购物车
    @Override
    public ShoppingCart subCart(ShoppingCart shoppingCart) {
        log.info("购物车数据:{}", shoppingCart);
        Long nowId = BaseContext.getCurrentId();
        shoppingCart.setUserId(nowId);

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, nowId);

        if (shoppingCart.getDishId() != null) {
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart shoppingCart1 = shoppingCartService.getOne(lambdaQueryWrapper);
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            if (shoppingCart1.getNumber() > 0) {
                shoppingCartService.updateById(shoppingCart1);
            }
            if (shoppingCart1.getNumber() == 0) {
                shoppingCartService.removeById(shoppingCart1.getId());
            }
            return shoppingCart1;
        }
        if (shoppingCart.getSetmealId() != null) {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            ShoppingCart shoppingCart2 = shoppingCartService.getOne(lambdaQueryWrapper);
            shoppingCart2.setNumber(shoppingCart2.getNumber() - 1);
            if (shoppingCart2.getNumber() > 0) {
                shoppingCartService.updateById(shoppingCart2);
            }
            if (shoppingCart2.getNumber() == 0) {
                shoppingCartService.removeById(shoppingCart2.getId());
            }
            return shoppingCart2;
        }
        return  null;
    }





    //查看购物车

    public List<ShoppingCart> list()
    {
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList=shoppingCartService.list(lambdaQueryWrapper);
        return shoppingCartList;
    }
}

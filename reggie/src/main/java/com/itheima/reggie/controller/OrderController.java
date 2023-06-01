package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }



    /**
     * 客户查询历史订单信息
     */
    @GetMapping("/userPage")
    public  R<Page> page (int page,int pageSize)
    {
      return   R.success(orderService.page(page,pageSize));
    }


    /**
     * 管理端后台查看订单信息
     */
@GetMapping("/page")
    public  R<Page> pageM(int page, int pageSize)
{
    return R.success(orderService.pageM(page,pageSize));
}



@PutMapping()
public  R<String> status(@RequestBody Orders orders)
{
    Orders orders1 = orderService.getById(orders.getId());
    orders1.setStatus(orders.getStatus());
    orderService.updateById(orders1);
    return  R.success("派送成功！！！");
}

}
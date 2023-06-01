package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;



    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }




    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }



    //管理端套餐分页查询
    @Override
    public Page<Setmeal> page(int page, int pageSize, String name) {
    Page<Setmeal> setmealPage =new Page<>(page,pageSize);
    Page<SetmealDto> setmealDtoPage =new Page<>();

    LambdaQueryWrapper<Setmeal> lambdaQueryWrapper =new LambdaQueryWrapper<>();
    lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
    lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
    this.page(setmealPage,lambdaQueryWrapper);             //1、查出所有setmeal数据



        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> setmealDtoList = records.stream().map( item ->{
            SetmealDto setmealDto =new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Category category =categoryService.getById(item.getCategoryId());  //2、分类名字赋给dto
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
                }
        ).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);
        return setmealPage;
    }





    @Override
    public List<SetmealDto> showSetmealDish(Long id) {
     LambdaQueryWrapper<Setmeal> lambdaQueryWrapper =new LambdaQueryWrapper<>();
     lambdaQueryWrapper.eq(Setmeal::getId,id);
     List<SetmealDto> setmealDtoList = new ArrayList<>();
     List<Setmeal>  setmeals;
     setmeals=this.list(lambdaQueryWrapper);
         setmealDtoList = setmeals.stream().map( item ->{
                    SetmealDto setmealDto =new SetmealDto();
                    BeanUtils.copyProperties(item,setmealDto);
                    Category category =categoryService.getById(item.getCategoryId());  //分类名字赋给dto
                    setmealDto.setCategoryName(category.getName());

                    LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper =new LambdaQueryWrapper<>();
                    setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,item.getId());
                    setmealDto.setSetmealDishes(setmealDishService.list(setmealDishLambdaQueryWrapper));
                    return setmealDto;
                }
        ).collect(Collectors.toList());

        return setmealDtoList;
    }
}

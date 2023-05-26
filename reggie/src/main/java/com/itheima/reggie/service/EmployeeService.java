package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.controller.EmployeeController;
import com.itheima.reggie.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;

public interface EmployeeService extends IService<Employee> {


    public  Page page(int page, int pageSize, String name);

}

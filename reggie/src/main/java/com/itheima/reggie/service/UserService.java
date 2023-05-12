package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.User;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {
    // 发送邮箱验证码
    Boolean sendMsg(User user, HttpSession session) throws MessagingException;
    // 移动端用户登录
    User login(Map<String, String> map, HttpSession session);

}

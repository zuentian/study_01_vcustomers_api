package com.example.zuer02.controller;

import com.example.zuer02.dao.UserDao;
import com.example.zuer02.entity.LoginAndUser;
import com.example.zuer02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController//Controller（视图解析器，拿到页面的数据）和ResponseBody（JSON等内容返回到页面）的结合，
@EnableAutoConfiguration
@RequestMapping(value="")
public class UserController {


    @Autowired
    UserDao userDao;


    public User obtainByUserId(String userId) {

        User userInfo=userDao.obtainByUserId(userId);

        if(userInfo==null){
            new  IllegalArgumentException("cannot find user by userId " + userId);
        }
        return userInfo;
    }

    public void insertUserInfo(User user) {

        userDao.insertUser(user);

    }

    public List<LoginAndUser> queryUserAndLogin(Map<String,Object> map) {
        return userDao.queryUserAndLogin(map);
    }
}
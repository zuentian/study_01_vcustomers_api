package com.example.zuer02.controller;

import com.example.zuer02.dao.UserDao;
import com.example.zuer02.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController//Controller（视图解析器，拿到页面的数据）和ResponseBody（JSON等内容返回到页面）的结合，
@EnableAutoConfiguration
@RequestMapping(value="")
public class UserController {


    @Autowired
    UserDao userDao;


    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/selectUser", method = RequestMethod.POST)
    public PageInfo<User> selectUser(@RequestBody Map<String, Object> param) throws Exception {
        //System.out.println(param);
        int page=Integer.valueOf(param.get("page").toString());
        int pageSize=Integer.valueOf(param.get("pageSize").toString());

        PageHelper.startPage(page,pageSize);
        PageHelper.orderBy("USER_CRT_DATE desc");
        List<User> userList = userDao.selectUser();
        PageInfo<User> pageInfo=new PageInfo<>(userList);
        //System.out.println(pageInfo);
        return pageInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public int addUser(@RequestBody Map<String, Object> param) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(param.get("customer"), User.class);

        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setUserCrtDate(sdf.format(new Date()));

        int i = userDao.insertUser(user);
        return i;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/editByUserId", method = RequestMethod.POST)
    public int editByUserId(@RequestBody Map<String, Object> param) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(param.get("customer"), User.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setUserAltDate(sdf.format(new Date()));

        System.out.println(user);
        int i=userDao.updateUserByUserId(user);
        return i;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/deleteUserByUserId", method=RequestMethod.GET)
    public int deleteUserByUserId(@RequestParam("userId") String userId) {
        int i=userDao.deleteUserByUserId(userId);
        return i;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/selectUserByUserId", method=RequestMethod.GET)
    public User selectUserByUserId(@RequestParam("userId") String userId) {
        User user=userDao.selectUserByUserId(userId);
        return user;
    }

    public static void main(String[] args) {
        System.out.println(111);
    }
}
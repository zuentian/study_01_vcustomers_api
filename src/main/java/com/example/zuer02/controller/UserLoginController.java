package com.example.zuer02.controller;


import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.assertj.core.util.Preconditions;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping(value="")
public class UserLoginController {

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/UserLogin/login", method = RequestMethod.POST)
    public String addAddressList(@RequestBody Map<String, Object> param) throws Exception {
        Object username=param.get("username");
        Object password=param.get("password");
        Preconditions.checkArgument(username != null, "请输入用户名");
        Preconditions.checkArgument(password != null, "请输入密码");
        login(new UsernamePasswordToken(String.valueOf(username), String.valueOf(password)));
        System.out.println(param);
        return "success";
    }

    private String  login(AuthenticationToken token) {

        return "";
    }

}

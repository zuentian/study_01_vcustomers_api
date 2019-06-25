package com.example.zuer02.controller;


import com.example.zuer02.entity.LoginInfo;
import com.example.zuer02.err.HippoServiceException;
import com.example.zuer02.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.assertj.core.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);



    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/UserLogin/login", method = RequestMethod.POST)
    public String addAddressList(@RequestBody Map<String, Object> param) throws Exception {
        System.out.println("登录信息："+param);
        Object username=param.get("username");
        Object password=param.get("password");

        Preconditions.checkArgument(username != null&&!"".equals(username), "请输入用户名");
        Preconditions.checkArgument(password != null&&!"".equals(password), "请输入密码");
        LoginInfo loginInfo=login(new UsernamePasswordToken(String.valueOf(username), String.valueOf(password)));
        System.out.println(loginInfo);
        return JWTUtil.sign(loginInfo.getPrincipal(), loginInfo.getCredential());
    }

    private LoginInfo  login(AuthenticationToken token) throws  Exception{
        try{
            System.out.println("token="+token.getCredentials()+" " +token.getPrincipal());
        SecurityUtils.getSubject().login(token);
    }catch (UnknownAccountException e) {
        LOGGER.error("账号不存在: {}", token);
        throw new HippoServiceException(401, "账号不存在");
    }catch (IncorrectCredentialsException e) {
        LOGGER.error("账号或密码错误: {}", token);
        throw new HippoServiceException(402, "账号或密码错误");
    }catch (LockedAccountException e) {
        LOGGER.error("账号被锁定: {}", token);
        throw new HippoServiceException(403, "该账号被锁定，请联系管理员");
    }catch (AuthenticationException e) {
        LOGGER.error("账号认证失败: {}", token);

        throw new HippoServiceException(404, "账号认证失败");
    }
        return (LoginInfo)SecurityUtils.getSubject().getPrincipal();
    }

}

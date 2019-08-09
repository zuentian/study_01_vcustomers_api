package com.example.zuer02.controller;

import com.example.zuer02.dao.LoginInfoDao;
import com.example.zuer02.entity.LoginAccountType;
import com.example.zuer02.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@EnableAutoConfiguration
@RequestMapping(value="")
public class LoginInfoController {

    @Autowired
    LoginInfoDao loginInfoDao;
    public LoginInfo obtainByTypeAndPrincipal(LoginAccountType type, String principal) throws IllegalArgumentException{

        System.out.println("type=["+type+"] principal=["+principal+"]");
        List<LoginInfo> loginInfoList=loginInfoDao.findAccountByTypeAndPrincipal(type.getCode(),principal);
        if(loginInfoList==null||loginInfoList.size()<=0){
            throw new IllegalArgumentException("cannot find account by type=" + type + " and principal=" + principal);
        }

        return loginInfoList.get(0);
        //return loginAccountDao.findAccountByTypeAndPrincipal(type.getCode(), principal)
                //.map(item -> Transformer.transform(item, LoginInfo.class))
                //.orElseThrow(() -> new IllegalArgumentException("cannot find account by type=" + type + " and principal=" + principal));
    }

    public void getLoginInfoByPrincipal(String principal)throws Exception {

        List<LoginInfo> loginInfoList=loginInfoDao.getLoginInfoByPrincipal(principal);
        if(loginInfoList!=null&&loginInfoList.size()>0){
            throw new IllegalArgumentException("该用户已注册！");
        }
    }

    public void insertLoginInfo(LoginInfo loginInfo) {
        loginInfoDao.insertLoginInfo(loginInfo);
    }
}

package com.example.zuer02.loginPower;

import com.example.zuer02.controller.LoginInfoController;
import com.example.zuer02.entity.LoginAccountType;
import com.example.zuer02.entity.LoginInfo;
import com.example.zuer02.entity.LoginStatus;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private LoginInfoController loginInfoController;
    @Autowired
    PasswordService passwordService;

    //获取角色权限信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("获取角色权限信息：" + principalCollection);
        return new SimpleAuthorizationInfo();
    }

    //获取用户凭证信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("获取用户凭证信息：" + authenticationToken);
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        LoginInfo loginInfo = loginInfoController.obtainByTypeAndPrincipal(LoginAccountType.MOBILE, username);
        DefaultPasswordService defaultPasswordService=new DefaultPasswordService();
        System.out.println("获取用户凭证信息loginInfo：" + loginInfo);
        //String passwordEncrypt = defaultPasswordService.encryptPassword(password);
        //System.out.println("加密结果：" + passwordEncrypt);
        //System.out.println("加密原来："+loginInfo.getCredential());
        if (!defaultPasswordService.passwordsMatch(password, loginInfo.getCredential()))
            throw new IncorrectCredentialsException();

        if (Objects.equals(loginInfo.getIsLocked(), LoginStatus.LOCKED))
            throw new LockedAccountException();
        return new SimpleAuthenticationInfo(loginInfo, password, getName());
    }

}

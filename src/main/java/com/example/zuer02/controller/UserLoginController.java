package com.example.zuer02.controller;

import com.example.zuer02.entity.*;
import com.example.zuer02.err.HippoServiceException;
import com.example.zuer02.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.assertj.core.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@EnableAutoConfiguration
@RequestMapping(value="")
public class UserLoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private LoginInfoController loginInfoController;

    @Autowired
    private UserController userController;

    @Transactional(rollbackFor = {Exception.class})
    //@RequestMapping(value = "/UserLogin/login", method = RequestMethod.POST)
    @RequestMapping(value = "/LoginService/login", method = RequestMethod.POST)
    public String addAddressList(@RequestBody Map<String, Object> param) throws Exception {
        System.out.println("登录信息："+param);
        Object username=param.get("username");
        Object password=param.get("password");

        Preconditions.checkArgument(username != null&&!"".equals(username), "请输入用户名");
        Preconditions.checkArgument(password != null&&!"".equals(password), "请输入密码");
        LoginInfo loginInfo=login(new UsernamePasswordToken(String.valueOf(username), String.valueOf(password)));

        return JWTUtil.sign(loginInfo.getPrincipal(), loginInfo.getCredential());//加密token
    }

    private LoginInfo  login(AuthenticationToken token) throws  Exception{
        try{
            System.out.println("UserLoginController token="+token.getCredentials()+" " +token.getPrincipal());
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

    @Transactional(rollbackFor = {Exception.class})
   // @RequestMapping(value = "/UserLogin/getCurrentUser",method = RequestMethod.POST)
    @RequestMapping(value = "/LoginService/getCurrentUser",method = RequestMethod.POST)
    public User getCurrentUser(@RequestBody Map<String, Object> param)throws Exception{

        String token=(String)param.get("token");
        Preconditions.checkArgument(token != null&&!"".equals(token), "token cannot be null!");

        System.out.println("getCurrentUser token=["+token+"] Username=["+JWTUtil.getUsername(token)+"]");
        LoginInfo loginInfo=loginInfoController.obtainByTypeAndPrincipal(LoginAccountType.MOBILE, JWTUtil.getUsername(token));

        System.out.println("getCurrentUser loginInfo=["+loginInfo+"]");
        if(!JWTUtil.verify(token, loginInfo.getPrincipal(), loginInfo.getCredential()))
            throw new HippoServiceException(401, "Token校验失败");
        if(Objects.equals(loginInfo.getIsLocked(), LoginStatus.LOCKED))
            throw new HippoServiceException(401, "该账号被锁定，请联系管理员");

        return userController.obtainByUserId(loginInfo.getId());
    }

    @Transactional(rollbackFor = {Exception.class})
    //@RequestMapping(value = "/UserLogin/logout",method = RequestMethod.POST)
    @RequestMapping(value = "/LoginService/logout",method = RequestMethod.POST)
    public void logout(@RequestBody Map<String, Object> param)throws Exception{
        SecurityUtils.getSubject().logout();

    }

    @Transactional(rollbackFor = {Exception.class})
    //@RequestMapping(value = "/UserLogin/register", method = RequestMethod.POST)

    @RequestMapping(value = "/LoginService/register", method = RequestMethod.POST)
    public String register(@RequestBody Map<String, Object> param) throws Exception {
        String username=(String)param.get("username");
        String password=(String)param.get("password");
        Preconditions.checkArgument(username != null&&!"".equals(username), "请输入用户名");
        Preconditions.checkArgument(password != null&&!"".equals(password), "请输入密码");
        loginInfoController.getLoginInfoByPrincipal(username);

        DefaultPasswordService defaultPasswordService=new DefaultPasswordService();
        String passwordEncrypt = defaultPasswordService.encryptPassword(password);
        LoginInfo loginInfo=new LoginInfo();
        String uuid=UUID.randomUUID().toString();
        loginInfo.setId(uuid);
        loginInfo.setType(LoginAccountType.MOBILE.getCode());
        loginInfo.setCredential(passwordEncrypt);
        loginInfo.setPrincipal(username);
        loginInfo.setIsLocked(LoginStatus.NORMAL.getCode());
        System.out.println(loginInfo);
        loginInfoController.insertLoginInfo(loginInfo);

        User user=new User();
        user.setUserId(uuid);
        userController.insertUserInfo(user);


        
        return "sucess";


    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/UserApiService/search",method = RequestMethod.POST)
    public Map<String,Object> search(@RequestBody Map<String, Object> param)throws Exception{
        try {
            String nameOrMobile=param.get("nameOrMobile")==null?null:(String)param.get("nameOrMobile");
            int pageNum= param.get("pageNum")==null?-1:(Integer)param.get("pageNum");
            int pageSize=param.get("pageSize")==null?-1:(Integer)param.get("pageSize");
            int status=param.get("status")==null?-1:(Integer)param.get("status");


            int start=(pageNum-1)*pageSize+1;
            int end=pageNum*pageSize;

            Map<String,Object> map=new HashMap<String,Object>();
            map.put("nameOrMobile",nameOrMobile);
            map.put("start",start);
            map.put("end",end);
            map.put("status",status);
            List<LoginAndUser> userInfoList=userController.queryUserAndLogin(map);
            if(userInfoList==null){
                throw new Exception("查无数据");
            }
            Map<String,Object> resultMap =new HashMap<String,Object>();
            resultMap.put("list",userInfoList);
            resultMap.put("count",userInfoList.size());
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage()) ;
        }

    }
}

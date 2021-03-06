package com.example.zuer02.dao;

import com.example.zuer02.entity.LoginAndUser;
import com.example.zuer02.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {


    User obtainByUserId(@Param("userId") String userId);

    void insertUser(User user);

    List<LoginAndUser> queryUserAndLogin(Map<String,Object> map);
}

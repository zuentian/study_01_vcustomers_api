package com.example.zuer02.dao;

import com.example.zuer02.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {


    List<User> selectUser();

    Page<User> selectUserByPage();

    int insertUser(User user);

    int updateUserByUserId(User user);

    int deleteUserByUserId(String userId);

    User selectUserByUserId(String userId);
}

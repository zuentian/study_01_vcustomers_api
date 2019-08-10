package com.example.zuer02.dao;

import com.example.zuer02.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {


    User obtainByUserId(@Param("userId") String userId);

    void insertUser(User user);
}

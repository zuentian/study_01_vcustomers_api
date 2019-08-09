package com.example.zuer02.dao;

import com.example.zuer02.entity.LoginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginInfoDao {
    List<LoginInfo> findAccountByTypeAndPrincipal(@Param("type") int code, @Param("principal") String principal);

    List<LoginInfo> getLoginInfoByPrincipal(@Param("principal") String principal);

    void insertLoginInfo(LoginInfo loginInfo);
}

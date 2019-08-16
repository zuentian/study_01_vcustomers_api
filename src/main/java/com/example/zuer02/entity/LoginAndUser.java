package com.example.zuer02.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LoginAndUser {


    private String  userId;
    private Integer status;
    private String userName;
    private String userNameBak;
    private String mobile;
    private Date crtTime;
    private Date altTime;
}

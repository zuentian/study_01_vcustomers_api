package com.example.zuer02.entity;

import lombok.Data;

@Data
public class LoginInfo {
    private Integer type;

    private Integer userId;

    private String principal;

    private String credential;

    private Integer isLocked;
}

package com.example.zuer02.entity;

import lombok.Data;

@Data
public class LoginInfo {

    private String  id;
    private Integer type;
    private String principal;
    private String credential;
    private Integer isLocked;
}

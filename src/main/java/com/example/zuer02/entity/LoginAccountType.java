package com.example.zuer02.entity;


import lombok.Getter;

@Getter
public enum  LoginAccountType {

    MOBILE(0, "手机号");

    private int code;

    private String message;

    LoginAccountType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

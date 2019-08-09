package com.example.zuer02.entity;

import lombok.Getter;

@Getter
public enum  LoginStatus {

    LOCKED(1, "账号被锁"),
    NORMAL(0, "账号正常");

    private int code;

    private String message;

    LoginStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

package com.example.zuer02.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String userId;
    private String userName;
    private String userNameBak;

}

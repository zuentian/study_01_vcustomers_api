package com.example.zuer02.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User implements Serializable {

    private String userId;
    private String userName;
    private String userNameBak;

    private String mobile;
    private List<Menu> resources;//该用的权限菜单

}

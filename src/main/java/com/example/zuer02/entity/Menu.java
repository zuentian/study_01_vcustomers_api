package com.example.zuer02.entity;


import lombok.Data;

@Data
public class Menu {

    private String menuId;
    private String title;
    private String code;
    private String menuParId;
    private int orderId;//排行
}

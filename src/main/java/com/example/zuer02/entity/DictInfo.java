package com.example.zuer02.entity;

import lombok.Data;

import java.util.Date;


@Data
public class DictInfo {

    private String dictId;
    private String dictType;
    private String dictTypeContent;
    private String dictCode;
    private String dictValue;
    private Date crtDate;
    private Date altDate;
}

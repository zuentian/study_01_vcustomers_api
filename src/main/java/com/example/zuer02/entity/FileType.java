package com.example.zuer02.entity;


public enum FileType {

    IMAGE("01", "image/jpeg"),

    EXCEL("02", "image/jpeg");

    private String code;

    private String message;

    FileType(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setIndex(String  message) {
        this.message = message;
    }
}

package com.example.zuer02.err;

public class ExceptionResponse {

    private String message;
    private Integer code;

    public ExceptionResponse(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public static ExceptionResponse create(Integer code, String message) {
        return new ExceptionResponse(code, message);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

package com.example.zuer02.err;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
@Slf4j
//@EnableWebMvc
public class ExceptionHandlerError {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleException(Exception ex) {
        if (ex instanceof HippoServiceException) {

            HippoServiceException hippoServiceException = (HippoServiceException) ex;
            return ExceptionResponse.create(hippoServiceException.getErrorCode(), hippoServiceException.getErrorMsg());
        } else {
            return ExceptionResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
    }
}

package com.example.zuer02;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
//有一些第三方的库需要注入bean，所以这里将要第三方的库路径写出来
@MapperScan(basePackages = {"org.apache.shiro.authc.credential","com.example.zuer02"})
public class Zuer02Application {

    public static void main(String[] args) {
        SpringApplication.run(Zuer02Application.class, args);
    }

}

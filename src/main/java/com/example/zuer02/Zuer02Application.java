package com.example.zuer02;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.zuer02.dao")
public class Zuer02Application {

    public static void main(String[] args) {
        SpringApplication.run(Zuer02Application.class, args);
    }

}

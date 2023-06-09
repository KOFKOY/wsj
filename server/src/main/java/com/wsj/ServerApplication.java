package com.wsj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRetry
@ComponentScan
@MapperScan(value = "com.wsj.server.mapper")
public class ServerApplication{

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}

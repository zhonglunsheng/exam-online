package com.exam.online;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.exam.online.mapper")
public class ExamOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamOnlineApplication.class, args);
    }

}


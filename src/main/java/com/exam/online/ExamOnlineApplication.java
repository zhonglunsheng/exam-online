package com.exam.online;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhonglunsheng
 * @Description 项目启动类
 * @create 2019-02-18 16:43
 */
@SpringBootApplication
@MapperScan("com.exam.online.mapper")
public class ExamOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamOnlineApplication.class, args);
    }

}


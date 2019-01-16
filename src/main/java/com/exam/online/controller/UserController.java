package com.exam.online.controller;


import com.exam.online.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 存储管理员、教师信息 前端控制器
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/index")
    public String index(){
        return "hello world~";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String email){
        log.info(email);
        return "hello world~";
    }

}


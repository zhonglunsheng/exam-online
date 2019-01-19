package com.exam.online.controller;


import cn.hutool.crypto.SecureUtil;
import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.exam.online.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员、教师相关操作
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(){
        return "hello world~";
    }

    /**
     * 管理员登陆
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/login")
    public Result login(User user, HttpServletRequest request){
        //账号和密码是否存在
        String email = user.getEmail();
        String password = user.getPassword();

        if (Strings.isBlank(email) || Strings.isBlank(password)){
            return Result.error("账号或密码错误");
        }

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("email", email);
        columnMap.put("password", password);
        List<User> list = (List<User>) userService.listByMap(columnMap);
        if (list.size() != 0){
            request.getSession().setAttribute("admin", user);
            return Result.success();
        }else{
            return Result.error("用户名不存在");
        }
    }


}


package com.exam.online.service.impl;

import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.exam.online.mapper.UserMapper;
import com.exam.online.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 存储管理员、教师信息 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(String email, String password) {
        Map<String, Object> inMap = new HashMap<>();
        inMap.put("email", email);
        inMap.put("password", password);
        List<User> result = userMapper.selectByMap(inMap);
        if (result.size() != 0){
            return Result.success(result.get(0));
        }else{
            return Result.error("账号或密码错误");
        }
    }
}

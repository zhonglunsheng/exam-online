package com.exam.online.service.impl;

import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.exam.online.mapper.PaperMapper;
import com.exam.online.mapper.QuestionMapper;
import com.exam.online.mapper.StudentMapper;
import com.exam.online.mapper.UserMapper;
import com.exam.online.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.online.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Result login(String email, String password, String role) {
        Map<String, Object> inMap = new HashMap<>(2);
        inMap.put("email", email);
        inMap.put("password", password);
        inMap.put("role", role);
        List<User> result = userMapper.selectByMap(inMap);
        if (result.size() != 0){
            return Result.success(result.get(0));
        }else{
            return Result.error("账号或密码错误");
        }
    }

    @Override
    public List<Long> getDataInfoCount() {
        List<Long> result = new ArrayList<>();
        List<User> users = userMapper.selectList(null);
        Long count = 0L;
        if (users == null || users.size() == 0){
            result.add(0L);
            result.add(0L);
        }else{
            for (User user:
                    users) {
                if (user != null){
                    count = user.getRole() == 0? count + 1:count;
                }
            }
            result.add(count);
            result.add(users.size() - count);
        }
        result.add(ParamCheck.getListNotNullSize(userMapper.selectList(null)));
        result.add(ParamCheck.getListNotNullSize(paperMapper.selectList(null)));
        result.add(ParamCheck.getListNotNullSize(questionMapper.selectList(null)));
        return result;
    }
}

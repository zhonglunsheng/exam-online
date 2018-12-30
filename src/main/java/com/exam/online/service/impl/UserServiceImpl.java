package com.exam.online.service.impl;

import com.exam.online.entity.User;
import com.exam.online.mapper.UserMapper;
import com.exam.online.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存储管理员、教师信息 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

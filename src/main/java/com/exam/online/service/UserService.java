package com.exam.online.service;

import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 存储管理员、教师信息 服务类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
public interface UserService extends IService<User> {

    Result login(String email, String password);
}

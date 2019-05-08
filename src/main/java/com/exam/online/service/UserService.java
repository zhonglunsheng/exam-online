package com.exam.online.service;

import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 存储管理员、教师信息 服务类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param email
     * @param password
     * @return
     */
    Result login(String email, String password, String role);

    List<Long> getDataInfoCount();
}

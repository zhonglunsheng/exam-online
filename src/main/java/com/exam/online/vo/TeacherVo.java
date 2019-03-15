package com.exam.online.vo;

import com.exam.online.entity.User;
import lombok.Data;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-15 17:19
 */
@Data
public class TeacherVo extends User {
    private String roleName;
}

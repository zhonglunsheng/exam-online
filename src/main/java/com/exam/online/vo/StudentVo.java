package com.exam.online.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.entity.Student;
import lombok.Data;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-11 19:28
 */
@Data
public class StudentVo extends Student {
    private String className;

    private String paperName;

    private Integer score;
}

package com.exam.online.vo;

import com.exam.online.entity.Student;
import lombok.Data;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-20 10:15
 */
@Data
public class CheckVo extends Student {
    private Integer paperId;
    private String className;
    private String status;
}

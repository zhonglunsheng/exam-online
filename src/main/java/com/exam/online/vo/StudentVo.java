package com.exam.online.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-11 19:28
 */
@Data
public class StudentVo {

    private Page page;
    private String name;
    private String num;
}

package com.exam.online.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.entity.Paper;
import com.exam.online.util.ParamCheck;


/**
 * web层通用数据处理
 */
public class BaseController {
    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 分页封装
     */
    public IPage startPage(){
        Integer pageSize = ParamCheck.getParamNotNullForInteger("pageSize");
        Integer pageNum = ParamCheck.getParamNotNullForInteger("pageNum");
        return new Page<Paper>(pageNum, pageSize);
    }
}

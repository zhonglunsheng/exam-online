package com.exam.online.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.online.bo.PageResult;
import com.exam.online.entity.Paper;

/**
 * <p>
 * 试卷信息表 服务类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
public interface PaperService extends IService<Paper> {

    Paper getPaperInfoForEdit(Integer id);

    PageResult getPaperList(QueryWrapper queryWrapper, IPage page);

    void paperSaveOrUpdate(Paper paper);
}

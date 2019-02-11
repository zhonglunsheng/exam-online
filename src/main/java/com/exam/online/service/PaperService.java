package com.exam.online.service;

import com.exam.online.common.Result;
import com.exam.online.entity.Paper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 试卷信息表 服务类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
public interface PaperService extends IService<Paper> {

    Result paperRandom(Integer paperId);
}

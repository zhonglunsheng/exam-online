package com.exam.online.service.impl;

import com.exam.online.common.Result;
import com.exam.online.entity.Paper;
import com.exam.online.mapper.PaperMapper;
import com.exam.online.service.PaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 试卷信息表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    @Override
    public Result paperRandom(Integer paperId) {
        return null;
    }
}

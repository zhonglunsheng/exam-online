package com.exam.online.service.impl;

import com.exam.online.entity.Score;
import com.exam.online.mapper.ScoreMapper;
import com.exam.online.service.ScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生成绩表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

}

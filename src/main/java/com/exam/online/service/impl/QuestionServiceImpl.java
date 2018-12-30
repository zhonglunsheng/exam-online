package com.exam.online.service.impl;

import com.exam.online.entity.Question;
import com.exam.online.mapper.QuestionMapper;
import com.exam.online.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问题信息表:默认选择题4个选项A/B/C/D 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

}

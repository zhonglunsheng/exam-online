package com.exam.online.service.impl;

import com.exam.online.entity.StudentScore;
import com.exam.online.mapper.StudentScoreMapper;
import com.exam.online.service.StudentScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生成绩表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Service
public class StudentScoreServiceImpl extends ServiceImpl<StudentScoreMapper, StudentScore> implements StudentScoreService {

}

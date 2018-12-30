package com.exam.online.service.impl;

import com.exam.online.entity.ExamRecord;
import com.exam.online.mapper.ExamRecordMapper;
import com.exam.online.service.ExamRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生考试记录表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {

}

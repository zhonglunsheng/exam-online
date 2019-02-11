package com.exam.online.service.impl;

import com.exam.online.entity.Record;
import com.exam.online.mapper.RecordMapper;
import com.exam.online.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生考试记录，教师阅卷表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

}

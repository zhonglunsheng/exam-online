package com.exam.online.service.impl;

import com.exam.online.entity.StudentClass;
import com.exam.online.mapper.StudentClassMapper;
import com.exam.online.service.StudentClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级信息表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class StudentClassServiceImpl extends ServiceImpl<StudentClassMapper, StudentClass> implements StudentClassService {

}

package com.exam.online.service.impl;

import com.exam.online.entity.Student;
import com.exam.online.mapper.StudentMapper;
import com.exam.online.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生信息表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}

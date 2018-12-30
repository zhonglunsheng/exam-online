package com.exam.online.service.impl;

import com.exam.online.entity.Class;
import com.exam.online.mapper.ClassMapper;
import com.exam.online.service.ClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级信息表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

}

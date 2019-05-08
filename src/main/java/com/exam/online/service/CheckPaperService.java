package com.exam.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.vo.exam.StudentPaperVo;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-04-22 16:58
 */
public interface CheckPaperService extends IService<PaperQuestion> {

    StudentPaperVo getPaperDetail(Integer studentId, Integer paperId);
}

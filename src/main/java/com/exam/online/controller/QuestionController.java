package com.exam.online.controller;


import com.exam.online.common.Consts;
import com.exam.online.common.Result;
import com.exam.online.entity.Question;
import com.exam.online.service.QuestionService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 问题信息表:默认选择题4个选项A/B/C/D 前端控制器
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Controller
@RequestMapping("/teacher/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 添加或更新问题
     * @param question
     * @return
     */
    @RequestMapping("/save")
    public Result questionAdd(Question question){
        if (question == null){
            return Result.error(Consts.COMOON.ARGUS_NULL);
        }

        if (Strings.isBlank(question.getTitle())){
            return Result.error(Consts.QUESTION.QUESTION_TITLE_NULL);
        }

        if (Strings.isBlank(question.getAnswer())){
            return Result.error(Consts.QUESTION.QUESTION_ANSWER_NULL);
        }

        questionService.saveOrUpdate(question);
        return Result.success();
    }

    /**
     * 删除问题
     * @param questionId
     * @return
     */
    @RequestMapping("/delete")
    public Result questionDelete(Integer questionId){
        if (questionId == null){
            return Result.error(Consts.PAPER.PAPER_ID_NULL);
        }

        Question question = questionService.getById(questionId);
        questionService.removeById(questionId);
        return Result.success();
    }
}


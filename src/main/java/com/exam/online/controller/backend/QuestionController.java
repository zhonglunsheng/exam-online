package com.exam.online.controller.backend;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.bo.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.entity.Question;
import com.exam.online.service.PaperQuestionService;
import com.exam.online.service.QuestionService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    private String viewName = "backend/question/";
    @Autowired
    private QuestionService questionService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    @GetMapping("/")
    public String main(){
        return viewName+"question";
    }

    @GetMapping("/add")
    public String add(){
        return viewName+"add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Integer id, Model model){
        Question question = questionService.getById(id);
        if (question.getOptionA() == null){
            question.setOptionA("");
        }
        if (question.getOptionB() == null){
            question.setOptionB("");
        }
        if (question.getOptionC() == null){
            question.setOptionC("");
        }
        if (question.getOptionD() == null){
            question.setOptionD("");
        }
        if (question.getOptionE() == null){
            question.setOptionE("");
        }
        model.addAttribute("question",question);
        return viewName+"edit";
    }

    /**
     * 删除题目
     * @param ids
     * @return
     */
    @PostMapping(value = "/remove")
    @ResponseBody
    public Result questionRemove(String ids){
        List<PaperQuestion> paperQuestionList = paperQuestionService.list(new QueryWrapper<PaperQuestion>().in("question_id", ids));
        if (paperQuestionList.size() == 0){
            questionService.removeByIds(CommonUtil.strToList(ids));
            return Result.success("操作成功");
        }else{
            return Result.success("删除失败！该题目有试卷调用");
        }
    }

    /**
     * 获取题库列表
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult questionList(HttpServletRequest request, Question question){
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        QueryWrapper<Question> queryWrapper= new QueryWrapper<>();
        IPage<Question> page = null;
        if (pageSize != null && pageNum != null){
            page = new Page<Question>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (question != null){
            if (!StringUtils.isBlank(question.getTitle())){
                queryWrapper.like("title", question.getTitle());
            }
            if (!StringUtils.isBlank(question.getType())){
                queryWrapper.like("type", question.getType());
            }
        }
        // 按字段排序
        if (!StringUtils.isBlank(orderByColumn)&&!StringUtils.isBlank(isAsc)){
            if (Consts.Query.CREATE_TIME.equals(orderByColumn)) {
                orderByColumn = "create_time";
            }
            if (Consts.Query.ASC.equals(isAsc)) {
                queryWrapper.orderByAsc(orderByColumn);
            }else{
                queryWrapper.orderByDesc(orderByColumn);
            }
        }
        page = questionService.page(page, queryWrapper);
        Integer total = questionService.list(queryWrapper).size();
        return PageResult.success(Long.valueOf(total), page.getRecords());
    }

    /**
     * 添加或更新问题
     * @param question
     * @return
     */
    @RequestMapping("/questionAdd")
    @ResponseBody
    public Result questionAdd(Question question){
        if (question.getId() == null){
            question.setCreateTime(DateTimeUtil.dateToStr(new Date()));
        }
        question.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        questionService.saveOrUpdate(question);
        return Result.success();
    }
}


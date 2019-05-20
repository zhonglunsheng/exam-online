package com.exam.online.controller.backend;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.bo.PageResult;
import com.exam.online.common.Result;
import com.exam.online.controller.BaseController;
import com.exam.online.entity.Paper;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.entity.Question;
import com.exam.online.service.PaperQuestionService;
import com.exam.online.service.PaperService;
import com.exam.online.service.QuestionService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.ParamCheck;
import com.exam.online.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 试卷生成 管理
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Controller
@RequestMapping("/admin/paper")
public class PaperController extends BaseController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    @Autowired
    private QuestionService questionService;

    private String viewName = "backend/paper/";

    @GetMapping("/")
    public String student(Model model) {
        return viewName + "paper";
    }

    @GetMapping("/add")
    public String add(Integer paperId, Model model) {
        return viewName + "add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("paper", paperService.getPaperInfoForEdit(id));
        return viewName + "edit";
    }

    @GetMapping("/findQuestion/{id}")
    public String findQuestion(@PathVariable(value = "id", required = false) Integer id, Model model) {
        RedisPoolUtil.set("paperId", id+"");
        return viewName + "qlist";
    }

    @GetMapping("/findQuestion/add")
    public String addQuestion(Model model) {
        return viewName + "addlist";
    }


    /**
     * 获取试卷列表
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult paperList(Paper paper) {
        IPage<Paper> page = startPage();
        QueryWrapper queryWrapper = (QueryWrapper) ParamCheck.getParamNotNullForObject("queryWrapper");
        // 根据条件查询
        if (paper != null) {
            if (!StringUtils.isBlank(paper.getName())) {
                queryWrapper.like("name", paper.getName());
            }
        }
        return paperService.getPaperList(queryWrapper, page);
    }




    /**
     * 添加或修改试卷 名称 题目类型 类型下题目数量 是否随机
     *
     * @return
     */
    @PostMapping("/paperAdd")
    @ResponseBody
    public Result paperSave(Paper paper) {
        // paper试卷名不能相同
        Integer count;
        if (paper.getId() == null){
            count = paperService.count(new QueryWrapper<Paper>().eq("name",paper.getName()));
            if (count > 0){
                return Result.error("试卷名不能相同");
            }
        }
        paperService.paperSaveOrUpdate(paper);
        return Result.success(Consts.Common.SUCCESS);
    }

    /**
     * 删除试卷
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/remove")
    @ResponseBody
    public Result studentRemove(String ids) {
        List<Paper> paperList = paperService.list(new QueryWrapper<Paper>().in("id", CommonUtil.strToList(ids)));
        for (Paper paper:
             paperList) {
            Boolean inExam = DateTimeUtil.whoIsBig(DateTimeUtil.dateToStr(new Date()), paper.getStartTime())
                    && DateTimeUtil.whoIsBig(paper.getEndTime(), DateTimeUtil.dateToStr(new Date()));
            // 该试卷正在考试中不能删除
            if (inExam){
                return Result.error(paper.getName()+"试卷正在考试中不能删除");
            }
        }
        boolean flag = paperService.removeByIds(CommonUtil.strToList(ids));
        // 删除改试卷下的所有关联的题目
        boolean result = paperQuestionService.remove(new QueryWrapper<PaperQuestion>().in("paper_id", CommonUtil.strToList(ids)));
        if (flag && result) {
            return Result.success(Consts.Common.SUCCESS);
        } else {
            return Result.error(Consts.Common.ERROR);
        }
    }

    /**
     * 查询试卷列表
     *
     * @param page
     * @return
     */
    @RequestMapping("/find")
    public Result paperList(Page page, String name) {
        if (page == null) {
            page = new Page();
        }

        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        if (Strings.isBlank(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(paperService.page(page, queryWrapper), Consts.Common.SUCCESS);
    }

    /**
     * 获取试卷题库列表
     *
     * @param request
     * @return
     */
    @PostMapping("/question/list/{operate}")
    @ResponseBody
    public PageResult questionList(@PathVariable("operate") String operate, HttpServletRequest request, Question question) {
        // 获取paperId
        String paperId = RedisPoolUtil.get("paperId");
        QueryWrapper<Question> queryWrapper = (QueryWrapper) ParamCheck.getParamNotNullForObject("queryWrapper");
        IPage<Question> page = startPage();
        // 根据条件查询
        if (question != null) {
            if (!StringUtils.isBlank(question.getTitle())) {
                queryWrapper.like("title", question.getTitle());
            }
            if (!StringUtils.isBlank(question.getType())) {
                queryWrapper.like("type", question.getType());
            }
        }
        Integer total = 0;
        List<Question> records = null;
        List<PaperQuestion> paperQuestions = paperQuestionService.list(new QueryWrapper<PaperQuestion>().eq("paper_id", paperId));
        List<Integer> questionIds = new ArrayList<>();
        if (paperQuestions != null) {
            total = paperQuestions.size();
        }
        if (paperQuestions != null) {
            for (PaperQuestion p :
                    paperQuestions) {
                questionIds.add(p.getQuestionId());
            }
        }
        if (Consts.Operate.SELECT.equals(operate)) {

            if (total != 0) {
                for (PaperQuestion p :
                        paperQuestions) {
                    questionIds.add(p.getQuestionId());
                }
                queryWrapper.in("id", questionIds);
                page = questionService.page(page, queryWrapper);
                records = page.getRecords();
            } else {
                records = new ArrayList<>();
            }
        } else {
            List<Question> questions = questionService.list((queryWrapper.notIn("id", questionIds)));
            if (questions != null) {
                total = questions.size();
            }
            page = questionService.page(page, queryWrapper.notIn("id", questionIds));
            records = page.getRecords();
        }

        return PageResult.success(Long.valueOf(total), records);
    }

    @PostMapping(value = "/question/add")
    @ResponseBody
    public Result questionAdd(String ids) {
        // todo
        List<Integer> idList = CommonUtil.strToList(ids);
        List<PaperQuestion> paperQuestions = new ArrayList<>();
        PaperQuestion paperQuestion = null;
        for (Integer id :
                idList) {
            paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(Integer.parseInt(RedisPoolUtil.get(Consts.RedisKey.ADMIN_PAPAER_ID)));
            paperQuestion.setQuestionId(id);
            paperQuestions.add(paperQuestion);
        }

        boolean result = paperQuestionService.saveBatch(paperQuestions);
        if (result) {
            return Result.success(Consts.Common.SUCCESS);
        } else {
            return Result.error(Consts.Common.ERROR);
        }
    }

    /**
     * 删除试卷题目
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/question/remove")
    @ResponseBody
    public Result questionRemove(String ids) {
        String paperId = RedisPoolUtil.get(Consts.RedisKey.ADMIN_PAPAER_ID);
        Paper paper = paperService.getById(paperId);
        Boolean inExam = DateTimeUtil.whoIsBig(DateTimeUtil.dateToStr(new Date()), paper.getStartTime())
                && DateTimeUtil.whoIsBig(paper.getEndTime(), DateTimeUtil.dateToStr(new Date()));
        // 该试卷正在考试中不能删除
        if (inExam){
            return Result.error(paper.getName()+"试卷正在考试中不能删除题目");
        }

        boolean result = paperQuestionService.remove(new QueryWrapper<PaperQuestion>().eq("paperId",paperId).in("question_id", CommonUtil.strToList(ids)));
        // 不在考试期间删除 需重新发布
        paper.setStatusCd(Consts.Paper.STATUS_CD_FAILURE);
        paperService.updateById(paper);
        if (result) {
            return Result.success(Consts.Common.SUCCESS);
        } else {
            return Result.error(Consts.Common.ERROR);
        }
    }

    /**
     * 考试发布
     * @param paperId
     * @return
     */
    @RequestMapping("/examRelease/{paperId}")
    @ResponseBody
    public Result examRelease(@PathVariable("paperId") Integer paperId){
        Paper paper = paperService.getById(paperId);
        if (paper.statusSuccessFlag()){
            // 已经发布了
            return Result.error("该试卷已发布成功，请不要重复发布");
        }
        int count = paperQuestionService.list(new QueryWrapper<PaperQuestion>()
                .eq("paper_id", paperId)).size();

        // todo 每个类型的题都要校验一遍
        String[] nums = paper.getTypeNums().split(",");
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += Integer.parseInt(nums[i]);
        }
        //todo mybatis-plus 多表关联查询
        if (count < total){
            return Result.error("该试卷题库数量不足,不能发布");
        }else{
            paper.setStatusCd(Consts.Paper.STATUS_CD_SUCCESS);
            paperService.updateById(paper);
            return Result.success("该试卷发布成功");
        }
    }

}


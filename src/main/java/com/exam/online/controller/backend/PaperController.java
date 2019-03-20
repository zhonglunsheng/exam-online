package com.exam.online.controller.backend;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.common.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.Paper;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.entity.Question;
import com.exam.online.entity.Student;
import com.exam.online.service.PaperQuestionService;
import com.exam.online.service.PaperService;
import com.exam.online.service.QuestionService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.RedisPoolUtil;
import com.exam.online.vo.PaperVo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.exam.online.common.Consts.Question.TYPENAME;

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
public class PaperController {

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
        Paper paper = null;
        StringBuffer typeBuff = new StringBuffer();
        if (id != null) {
            paper = paperService.getById(id);
            paper.setState(1);
            paperService.updateById(paper);
            String[] typeName = paper.getType().split(",");
            for (int i = 0; i < TYPENAME.length; i++) {
                if (Integer.parseInt(typeName[i]) != 0){
                    typeBuff.append(TYPENAME[i]);
                }
            }

            paper.setType(typeBuff.toString());
        }
        model.addAttribute("paper", paper);
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
     *
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult paperList(HttpServletRequest request, Paper paper) {
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        QueryWrapper queryWrapper = new QueryWrapper<Student>();
        IPage<Paper> page = null;
        List<PaperVo> rows = new ArrayList<>();
        if (pageSize != null && pageNum != null) {
            page = new Page<Paper>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (paper != null) {
            if (!StringUtils.isBlank(paper.getName())) {
                queryWrapper.like("name", paper.getName());
            }
        }
        // 按字段排序
        if (!StringUtils.isBlank(orderByColumn) && !StringUtils.isBlank(isAsc)) {
            if (Consts.Query.CREATE_TIME.equals(orderByColumn)) {
                orderByColumn = "create_time";
            }
            if (Consts.Query.ASC.equals(isAsc)) {
                queryWrapper.orderByAsc(orderByColumn);
            } else {
                queryWrapper.orderByDesc(orderByColumn);
            }
        }
        Integer total = 0;
        List<Paper> papers = paperService.list();
        if (papers != null) {
            total = papers.size();
        }
        page = paperService.page(page, queryWrapper);
        List<Paper> records = page.getRecords();
        PaperVo paperVo = null;
        rows = getPaperVoList(rows, records);
        return PageResult.success(Long.valueOf(total), rows);
    }

    /**
     * paperVo生成
     *
     * @param rows
     * @param records
     * @return
     */
    private List<PaperVo> getPaperVoList(List<PaperVo> rows, List<Paper> records) {
        PaperVo paperVo;
        if (records != null) {
            for (Paper s :
                    records) {
                StringBuffer typeName = new StringBuffer();
                StringBuffer typeNum = new StringBuffer();
                StringBuffer scoreBuff = new StringBuffer();
                String[] types = new String[5];
                String[] typeNums = new String[5];
                String[] scores = new String[5];
                Integer totalScore = 0;
                paperVo = new PaperVo();
                BeanUtils.copyProperties(s, paperVo);

                String type = s.getType();
                String nums = s.getTypeNums();
                String score = s.getScore();
                if (!StringUtils.isBlank(type)) {
                    types = type.split(",");
                    typeNums = nums.split(",");
                    scores = score.split(",");
                }
                for (int i = 0; i < types.length; i++) {
                    if ("1".equals(types[i])) {
                        switch (i) {
                            case 0:
                                typeName.append("单选 ");
                                typeNum.append(typeNums[i] + " ");
                                scoreBuff.append(scores[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 1:
                                typeName.append("多选 ");
                                typeNum.append(typeNums[i] + " ");
                                scoreBuff.append(scores[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 2:
                                typeName.append("判断 ");
                                typeNum.append(typeNums[i] + " ");
                                scoreBuff.append(scores[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 3:
                                typeName.append("简答 ");
                                scoreBuff.append(scores[i] + " ");
                                typeNum.append(typeNums[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 4:
                                typeName.append("应用 ");
                                scoreBuff.append(scores[i] + " ");
                                typeNum.append(typeNums[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            default:
                        }
                    }
                }
                paperVo.setTypeName(typeName.toString());
                paperVo.setTypeNums(typeNum.toString());
                paperVo.setScore(scoreBuff.toString());
                paperVo.setTotalScore(totalScore);
                paperVo.setStatus(s.getState() == 0 ? "已发布":"未发布");
                rows.add(paperVo);
            }
        }
        return rows;
    }


    /**
     * 添加或修改试卷 名称 题目类型 类型下题目数量 是否随机
     *
     * @return
     */
    @PostMapping("/paperAdd")
    @ResponseBody
    public Result paperSave(Paper paper) {
        paper.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        // 字段拆分转换
        String[] scores = paper.getScore().split(",");
        String[] typeNums = paper.getTypeNums().split(",");
        String[] types = paper.getType().split(",");

        // paper试卷名不能相同
        Integer count;
        if (paper.getId() == null){
            count = paperService.count(new QueryWrapper<Paper>().eq("name",paper.getName()));
            if (count > 0){
                return Result.error("试卷名不能相同");
            }
        }

        if (paper.getId() == null){
            paper.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            paper.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        }else{
            paper.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        }

        int[] typeInt = new int[5];
        int[] scoreInt = new int[5];
        int[] typeNumsInt = new int[5];
        int total = 0;
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
                case "单选题":
                    typeInt[0] = 1;
                    scoreInt[0] = Integer.parseInt(scores[0]);
                    typeNumsInt[0] = Integer.parseInt(typeNums[0]);
                    total += scoreInt[0] * typeNumsInt[0];
                    break;
                case "多选题":
                    typeInt[1] = 1;
                    scoreInt[1] = Integer.parseInt(scores[1]);
                    typeNumsInt[1] = Integer.parseInt(typeNums[1]);
                    total += scoreInt[1] * typeNumsInt[1];
                    break;
                case "判断题":
                    typeInt[2] = 1;
                    scoreInt[2] = Integer.parseInt(scores[2]);
                    typeNumsInt[2] = Integer.parseInt(typeNums[2]);
                    total += scoreInt[2] * typeNumsInt[2];
                    break;
                case "简答题":
                    typeInt[3] = 1;
                    scoreInt[3] = Integer.parseInt(scores[3]);
                    typeNumsInt[3] = Integer.parseInt(typeNums[3]);
                    total += scoreInt[3] * typeNumsInt[3];
                    break;
                case "应用题":
                    typeInt[4] = 1;
                    scoreInt[4] = Integer.parseInt(scores[4]);
                    typeNumsInt[4] = Integer.parseInt(typeNums[4]);
                    total += scoreInt[4] * typeNumsInt[4];
                    break;
                default:
            }
        }
        String type = CommonUtil.arrayForIntToStr(typeInt);
        String score = CommonUtil.arrayForIntToStr(scoreInt);
        String typeNum = CommonUtil.arrayForIntToStr(typeNumsInt);
        paper.setType(type);
        paper.setScore(score);
        paper.setTypeNums(typeNum);

        paperService.saveOrUpdate(paper);

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
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        // todo 前端添加入参
        String paperId = RedisPoolUtil.get("paperId");
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        IPage<Question> page = null;
        if (pageSize != null && pageNum != null) {
            page = new Page<Question>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (question != null) {
            if (!StringUtils.isBlank(question.getTitle())) {
                queryWrapper.like("title", question.getTitle());
            }
            if (!StringUtils.isBlank(question.getType())) {
                queryWrapper.like("type", question.getType());
            }
        }
        // 按字段排序
        if (!StringUtils.isBlank(orderByColumn) && !StringUtils.isBlank(isAsc)) {
            if (Consts.Query.CREATE_TIME.equals(orderByColumn)) {
                orderByColumn = "create_time";
            }
            if (Consts.Query.ASC.equals(isAsc)) {
                queryWrapper.orderByAsc(orderByColumn);
            } else {
                queryWrapper.orderByDesc(orderByColumn);
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
            paperQuestion.setPaperId(Integer.parseInt(RedisPoolUtil.get("paperId")));
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
     * 删除试卷信息
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/question/remove")
    @ResponseBody
    public Result questionRemove(String ids) {
        boolean result = paperQuestionService.remove(new QueryWrapper<PaperQuestion>().in("question_id", CommonUtil.strToList(ids)));
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
        if (paper.getState() == 0){
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

        if (count < total){
            return Result.error("该试卷题库数量不足,不能发布");
        }else{
            paper.setState(0);
            paperService.updateById(paper);
            return Result.success("该试卷发布成功");
        }
    }

}


package com.exam.online.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.common.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.*;
import com.exam.online.service.*;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.vo.CheckVo;
import com.exam.online.vo.PaperVo;
import com.exam.online.vo.exam.QuestionResult;
import com.exam.online.vo.exam.QuestionVo;
import com.exam.online.vo.exam.StudentPaperVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-19 10:05
 */
@Controller
@RequestMapping("/teacher/check")
public class CheckPaperController {

    private final String viewName = "backend/teacher/check/";

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("classList", studentClassService.list());
        model.addAttribute("paperList", paperService.list());
        return viewName + "main";
    }

    @GetMapping("/add")
    public String add() {
        return viewName + "add";
    }

    /**
     * 阅卷详情
     * @param model
     * @param studentId
     * @param paperId
     * @return
     */
    @GetMapping("/detail")
    public String detail(Model model, Integer studentId, Integer paperId) {
        StudentPaperVo studentPaperVo = new StudentPaperVo();
        if (studentId == null || paperId == null){
            return "redirct:/err500";
        }else{
            studentPaperVo.setStudentId(studentId);
            studentPaperVo.setPaperId(paperId);
        }
        List<QuestionResult> questionResultList = new ArrayList<>();

        // 补充逻辑 1、先获取试卷配置表里面的信息   2、按照顺序分别取出单项、多项、判断、简答、应用
        Paper paper = paperService.getById(paperId);
        String[] type = paper.getType().split(",");
        String[] num = paper.getTypeNums().split(",");
        String[] score = paper.getScore().split(",");
        String[] typeName = new String[]{"单选题", "多选题", "判断", "简答", "应用"};
        List<QuestionVo> questionVos;
        // 获取该学生考试的题库记录
        List<Record> recordList = recordService.list(new QueryWrapper<Record>()
                .eq("student_id", studentId)
                .eq("paper_id", paperId));
        List<Integer> ids = new ArrayList<>();
        // 没有考试提交记录 该考生缺考默认0分
        for (Record d :
                recordList) {
            ids.add(d.getQuestionId());
        }
        if (ids.size() == 0) {
            studentPaperVo.setCode(1);
            studentPaperVo.setMsg("该学生本次考试缺考，默认计算0分");
            Score score1 = new Score();
            score1.setPaperId(paperId);
            score1.setStudentId(studentId);
            score1.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            score1.setStudentScore(0);
            score1.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
            scoreService.save(score1);
            model.addAttribute("result", studentPaperVo);
            return viewName + "detail";
        }
        QuestionResult questionResult;
        for (int i = 0; i < type.length; i++) {
            questionResult = new QuestionResult();
            if ("1".equals(type[i])) {
                questionResult.setName(typeName[i]);
                questionResult.setScore(Integer.parseInt(score[i]) * Integer.parseInt(num[i]));
                questionResult.setTotal(Integer.parseInt(num[i]));
                questionResult.setType(i);

                questionVos = new ArrayList<>();
                QuestionVo vo = new QuestionVo();

                List<Question> questions = questionService.list(new QueryWrapper<Question>()
                        .eq("type", typeName[i]).in("id", ids));

                for (Question n :
                        questions) {
                    Record record = recordService.getOne(new QueryWrapper<Record>().eq("question_id", n.getId()).eq("paper_id",paperId));
                    vo.setTitle(n.getTitle());
                    vo.setOptionA(n.getOptionA().replace("&nbsp;", ""));
                    vo.setOptionB(n.getOptionB().replace("&nbsp;", ""));
                    vo.setOptionC(n.getOptionC().replace("&nbsp;", ""));
                    vo.setOptionD(n.getOptionD().replace("&nbsp;", ""));
                    vo.setOptionE(n.getOptionE().replace("&nbsp;", ""));
                    vo.setStudentAnswer(record.getStudentAnswer());
                    vo.setAnswer(n.getAnswer());
                    vo.setTargetScore(Integer.parseInt(score[i]));
                    vo.setSingleScore(n.getAnswer().equals(record.getStudentAnswer()) ? Integer.parseInt(score[i]) : 0);
                    questionVos.add(vo);
                }
                questionResult.setQuestionVos(questionVos);
                questionResultList.add(questionResult);
            }
        }
        studentPaperVo.setCode(0);
        studentPaperVo.setPaperName(paper.getName());
        studentPaperVo.setQuestionResults(questionResultList);
        model.addAttribute("result", studentPaperVo);
        return viewName + "detail";
    }

    /**
     * 获取试卷列表
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/list")
    @ResponseBody
    public PageResult paperList(HttpServletRequest request, CheckVo checkReq) {
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        QueryWrapper queryWrapper = new QueryWrapper<Student>();
        Boolean flag = false;
        IPage<Student> page = new Page<>();
        List<PaperVo> rows = new ArrayList<>();
        if (pageSize != null && pageNum != null) {
            page = new Page<>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (checkReq != null) {
            if (checkReq.getClassId() != null) {
                queryWrapper.like("class_id", checkReq.getClassId());
            }

        }
        // 返回查询未阅卷的学生列表逻辑  去成绩表查看试卷分数没有成绩的学生是待阅卷状态

        // 知道paperId

        // 判断考试是否已经结束了
        Paper paper = paperService.getById(checkReq.getPaperId());
        String endTime = paper.getEndTime();
        Date time = DateTimeUtil.strToDate(endTime);
        boolean status = System.currentTimeMillis() - time.getTime() > 0;
        if (!status){
            return PageResult.success(0L, new ArrayList());
        }
        List<Score> scoreList = scoreService.list(new QueryWrapper<Score>().eq("paper_id", Objects.requireNonNull(checkReq).getPaperId()));
        List<Integer> ids = new ArrayList<>();
        for (Score e :
                scoreList) {
            ids.add(e.getStudentId());
        }

        int total;
        total = studentService.list(new QueryWrapper<Student>().notIn("student_id", ids)).size();
        page = studentService.page(page, (Wrapper<Student>) queryWrapper.notIn("student_id", ids));

        // 封装返回checkVo
        List<CheckVo> checkVos = new ArrayList<>();
        for (Student t :
                page.getRecords()) {
            CheckVo checkVo = new CheckVo();
            BeanUtils.copyProperties(t, checkVo);
            checkVo.setClassName(studentClassService.getById(t.getClassId()).getClassName());
            checkVo.setStatus("待阅卷");
            checkVos.add(checkVo);
        }

        List<Paper> papers = paperService.list();

        return PageResult.success(Long.valueOf(total), checkVos);
    }

    /**
     * 提交阅卷后分数
     * @param score
     * @param studentId
     * @param paperId
     * @return
     */
    @RequestMapping("/getScore")
    @ResponseBody
    public Result getScore(Integer score, Integer studentId, Integer paperId){
        Score param = new Score();
        param.setCreateTime(DateTimeUtil.dateToStr(new Date()));
        param.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        param.setStudentScore(score);
        param.setStudentId(studentId);
        param.setPaperId(paperId);

        boolean result = scoreService.save(param);
        if (result){
            return Result.success(Consts.Common.SUCCESS);
        }else{
            return Result.error(Consts.Common.ERROR);
        }
    }
}

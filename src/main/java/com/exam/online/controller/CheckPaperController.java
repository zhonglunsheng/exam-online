package com.exam.online.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.bo.PageResult;
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
public class CheckPaperController extends BaseController{

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

    @Autowired
    private CheckPaperService checkPaperService;

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
        if (studentId == null || paperId == null){
            return redirect("/500");
        }
        model.addAttribute("result", checkPaperService.getPaperDetail(studentId, paperId));
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
        QueryWrapper queryWrapper = new QueryWrapper<Student>();
        IPage<Student> page = startPage();

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

        return PageResult.success(Long.valueOf(total), checkVos);
    }

    /**
     * 提交阅卷后分数
     * @param score
     * @return
     */
    @RequestMapping("/getScore")
    @ResponseBody
    public Result getScore(Score score){
        Score commitScore = new Score();
        commitScore.setCreateTime(DateTimeUtil.dateToStr(new Date()));
        commitScore.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        commitScore.setStudentScore(score.getStudentScore());
        commitScore.setStudentId(score.getStudentId());
        commitScore.setPaperId(score.getPaperId());
        commitScore.setFullScore(score.getFullScore());

        boolean result = scoreService.save(commitScore);
        if (result){
            return Result.success(Consts.Common.SUCCESS);
        }else{
            return Result.error(Consts.Common.ERROR);
        }
    }
}

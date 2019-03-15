package com.exam.online.controller.backend;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.ChartsResult;
import com.exam.online.common.PageResult;
import com.exam.online.common.Series;
import com.exam.online.entity.Paper;
import com.exam.online.entity.Score;
import com.exam.online.entity.Student;
import com.exam.online.entity.StudentClass;
import com.exam.online.service.PaperService;
import com.exam.online.service.ScoreService;
import com.exam.online.service.StudentClassService;
import com.exam.online.service.StudentService;
import com.exam.online.vo.ScoreVo;
import com.exam.online.vo.StudentVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学生成绩表 前端控制器
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Controller
@RequestMapping("/teacher/score")
public class ScoreController {

    private String viewName = "backend/teacher/score/";

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ScoreService scoreService;

    @RequestMapping("/")
    public String main(Model model) {
        List<Paper> papers = paperService.list(new QueryWrapper<Paper>().orderByDesc("create_time"));
        if (papers != null && papers.size() != 0){
            model.addAttribute("paperId", papers.get(0).getId());
        }
        model.addAttribute("classList", studentClassService.list());
        model.addAttribute("paperList", paperService.list());
        return viewName + "main";
    }

    @GetMapping("/data/{id}")
    @ResponseBody
    public ChartsResult getRange(@PathVariable("id") Integer id) {
        ChartsResult chartsResult = new ChartsResult();

        List<String> categories = new ArrayList<>();
        categories.add("优秀");
        categories.add("良好");
        categories.add("及格");
        categories.add("不及格");

        List<StudentClass> classList = studentClassService.list();
        List<String> legendList = new ArrayList<>();
        List<Series> seriesList = new ArrayList<>();
        Integer excellent = 0, good = 0, pass = 0, fail = 0;
        for (StudentClass s :
                classList) {
            // 查询该班学生id列表
            List<Student> students = studentService.list(new QueryWrapper<Student>().eq("class_id", s.getClassId()));
            if (students.size() != 0) {
                List<Integer> ids = new ArrayList<>();
                for (Student t :
                        students) {
                    ids.add(t.getStudentId());
                }
                // 查询不同分段的数量
                excellent = scoreService.count(new QueryWrapper<Score>()
                        .le("student_score", 100)
                        .ge("student_score", 90)
                        .eq("paper_id", id)
                        .in("student_id", ids));

                good = scoreService.count(new QueryWrapper<Score>()
                        .le("student_score", 89)
                        .ge("student_score", 70)
                        .eq("paper_id", id)
                        .in("student_id", ids));

                pass = scoreService.count(new QueryWrapper<Score>()
                        .ge("student_score", 60)
                        .eq("paper_id", id)
                        .in("student_id", ids));

                fail = scoreService.count(new QueryWrapper<Score>()
                        .lt("student_score", 60)
                        .eq("paper_id", id)
                        .in("student_id", ids));
            }
            Series series = new Series();
            List<Integer> data = new ArrayList<>();

            data.add(excellent);
            data.add(good);
            data.add(pass);
            data.add(fail);
            series.setData(data);
            series.setName(s.getClassName());

            seriesList.add(series);
            legendList.add(s.getClassName());
        }

        chartsResult.setTitle(legendList);
        chartsResult.setCategories(categories);
        chartsResult.setSeries(seriesList);
        return chartsResult;
    }

    /**
     * 获取学生列表
     *
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult classList(HttpServletRequest request, Student student, Integer paperId) {
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        QueryWrapper queryWrapper = new QueryWrapper<Student>();
        IPage<Score> page = null;
        if (pageSize != null && pageNum != null) {
            page = new Page<Score>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (student != null) {
            if (!StringUtils.isBlank(student.getNum())) {
                queryWrapper.like("num", student.getNum());
            }
            if (!StringUtils.isBlank(student.getName())) {
                queryWrapper.like("name", student.getName());
            }
            if (student.getClassId() != null) {
                queryWrapper.eq("class_id", student.getClassId());
            }
        }
        Integer total = 0;
        List<Student> records = studentService.list(queryWrapper);
        if (records == null || records.size() == 0){
            return PageResult.success(0L, new ArrayList());
        }

        // 获取需要查找的id
        List<Integer> ids = new ArrayList<>();
        for (Student s :
                records) {
            ids.add(s.getStudentId());
        }

        ScoreVo scoreVo = null;
        List<ScoreVo> scores = new ArrayList<>();

        queryWrapper = new QueryWrapper<Score>().in("student_id", ids);
        total = scoreService.list(queryWrapper).size();

        if (paperId != null) {
            queryWrapper.eq("paper_id", paperId);
        }
        page = scoreService.page(page, queryWrapper);
        if (page.getRecords() != null && page.getRecords().size() != 0) {
            // 获取查找的分数记录
            for (Score e :
                    page.getRecords()) {
                scoreVo = new ScoreVo();
                Student s = studentService.getById(e.getStudentId());
                BeanUtils.copyProperties(s, scoreVo);
                Integer classId = s.getClassId();
                if (classId != null) {
                    StudentClass studentClass = studentClassService.getById(classId);
                    if (studentClass != null) scoreVo.setClassName(studentClass.getClassName());
                }
                scoreVo.setPaperName(paperService.getById(e.getPaperId()).getName());
                scoreVo.setScore(e.getStudentScore());
                scores.add(scoreVo);
            }
        }
        return PageResult.success(Long.valueOf(total), scores);
    }

}


package com.exam.online.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exam.online.access.UserContext;
import com.exam.online.common.Consts;
import com.exam.online.common.Result;
import com.exam.online.entity.*;
import com.exam.online.service.*;
import com.exam.online.util.*;
import com.exam.online.vo.ScoreVo;
import com.exam.online.vo.exam.QuestionResult;
import com.exam.online.vo.exam.QuestionVo;
import com.exam.online.vo.exam.StudentPaperVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author zhonglunsheng
 * @Description 学生前端
 * @create 2019-02-27 18:57
 */
@Controller
@Slf4j
public class StudentFrontController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentClassService studentClassService;

    /**
     * 学生登录首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        List<StudentClass> classList = studentClassService.list();
        model.addAttribute("classList", classList);
        return "loginS2";
    }

    /**
     * 检查学号是否已存在
     *
     * @param num
     * @return
     */
    @RequestMapping("/checkNum")
    @ResponseBody
    public Result checkNum(String num) {
        Integer result = studentService.count(new QueryWrapper<Student>().eq("num", num));
        if (result > 0) {
            return Result.error("学号已存在");
        } else {
            return Result.success();
        }
    }

    /**
     * 学生登录
     *
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Result register() {
        try {
            String num = ParamCheck.getParamNotNullForString("num");
            Integer result = studentService.count(new QueryWrapper<Student>().eq("num", num));
            if (result > 0) {
                return Result.error("学号已存在");
            }
            String password = ParamCheck.getParamNotNullForString("password");
            String name = ParamCheck.getParamNotNullForString("name");
            String classId = ParamCheck.getParamNotNullForString("classId");
            Student student = new Student();
            student.setNum(num);
            student.setPassword(Md5Util.md5Encodeutf8(password));
            student.setName(name);
            student.setClassId(Integer.parseInt(classId));
            student.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            student.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
            studentService.save(student);

            return Result.success("注册成功");
        } catch (Exception e) {
            log.error("注册异常 {}", e);
            return Result.error("注册失败");
        }
    }

    /**
     * 学生考试登录
     *
     * @param student
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Result doLogin(Student student, HttpServletResponse response) {
        String num = student.getNum();
        String password = student.getPassword();
        password = Md5Util.md5Encodeutf8(password);
        List<Student> studentList = studentService.list(new QueryWrapper<Student>()
                .eq("password", password)
                .eq("num", num));

        if (studentList != null && studentList.size() != 0) {
            String token = UUID.randomUUID().toString();
            RedisPoolUtil.setEx(token, JSON.toJSONString(studentList.get(0)), 60*30);
            CookieUtil.writeLoginToken(response, Consts.Common.LOGIN_TOKEN, token);
            return Result.success();
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 跳转学生平台首页
     *
     * @return
     */
    @RequestMapping("/student/main")
    public String main(Model model, HttpServletRequest request) {
        Student student = (Student) UserContext.getUser();
        List<Score> scoreList = scoreService.list(new QueryWrapper<Score>().eq("student_id", student.getStudentId()));
        List<Record> recordList = recordService.list(new QueryWrapper<Record>().eq("student_id", student.getStudentId()));
        List<Paper> list = null;
        List<Integer> ids = new ArrayList<>();
        for (Record d :
                recordList) {
            ids.add(d.getPaperId());
        }

        for (Score e :
                scoreList) {
            ids.add(e.getPaperId());
        }

        list = paperService.list(new QueryWrapper<Paper>().gt("end_time", new Date())
                .notIn("id", ids));

        model.addAttribute("examList", list);
        model.addAttribute("student", student);
        return "main";
    }

    @RequestMapping("/student/detail/check/{id}")
    @ResponseBody
    public Result paperDetail(@PathVariable("id") Integer paperId) {
        Paper paper = paperService.getById(paperId);
        Boolean inExam = DateTimeUtil.whoIsBig(DateTimeUtil.dateToStr(new Date()), paper.getStartTime());
        if (inExam){
            return Result.success();
        }else{
            return Result.error();
        }
    }


    @RequestMapping("/student/detail/{id}")
    public String paperDetail(@PathVariable("id") Integer paperId, Model model, HttpSession session) {
        // 获取试卷配置 题型 数量 分数
        Paper paper = paperService.getById(paperId);
        if (paper == null) {
            return "detail";
        }
        String[] type = paper.getType().split(",");
        String[] num = paper.getTypeNums().split(",");
        String[] score = paper.getScore().split(",");
        String[] typeName = Consts.Question.TYPE_NAME;

        // 获取试卷题库
        List<PaperQuestion> paperQuestions = paperQuestionService.list(new QueryWrapper<PaperQuestion>().eq("paper_id", paperId));
        Integer total = 0;
        for (int i = 0; i < num.length; i++) {
            total += Integer.parseInt(num[i]);
        }
        if (paperQuestions.size() == 0 || paperQuestions.size() < total) {
            model.addAttribute("student", (Student) UserContext.getUser());
            return "error/err500";
        }
        List<Integer> ids = new ArrayList<>();
        for (PaperQuestion n :
                paperQuestions) {
            ids.add(n.getQuestionId());
        }

        Integer questionNum = null;
        Random random = new Random();
        Student student = (Student) UserContext.getUser();
        StudentPaperVo paperVo = new StudentPaperVo();
        paperVo.setPaperId(paperId);
        paperVo.setStudentId(student.getStudentId());
        List<QuestionResult> questionResults = new ArrayList<>();

        for (int i = 0; i < type.length; i++) {
            if (Integer.parseInt(type[i]) != 0) {
                questionNum = Integer.parseInt(num[i]);
                QuestionResult result = new QuestionResult();
                result.setType(i);
                result.setName(typeName[i]);
                result.setTotal(questionNum);
                result.setScore(questionNum * Integer.parseInt(score[i]));
                List<QuestionVo> questionVos = new ArrayList<>();
                List<Question> questions = (List<Question>) questionService
                        .list(new QueryWrapper<Question>()
                                .in("id", ids).eq("type", typeName[i]));
                while (questionVos.size() < questionNum && questions.size() != 0) {
                    // 随机获取
                    int rand = random.nextInt(questions.size());
                    Question q = questions.get(rand);
                    QuestionVo qVo = new QuestionVo();
                    qVo.setTargetScore(Integer.parseInt(score[i]));
                    BeanUtils.copyProperties(q, qVo);
                    qVo.setOptionA(qVo.getOptionA() != null ? qVo.getOptionA().replace("&nbsp;", "") : qVo.getOptionA());
                    qVo.setOptionB(qVo.getOptionB() != null ? qVo.getOptionB().replace("&nbsp;", "") : qVo.getOptionB());
                    qVo.setOptionC(qVo.getOptionC() != null ? qVo.getOptionC().replace("&nbsp;", "") : qVo.getOptionC());
                    qVo.setOptionD(qVo.getOptionD() != null ? qVo.getOptionD().replace("&nbsp;", "") : qVo.getOptionD());
                    qVo.setOptionE(qVo.getOptionE() != null ? qVo.getOptionE().replace("&nbsp;", "") : qVo.getOptionE());
                    if (!questionVos.contains(qVo)) {
                        questionVos.add(qVo);
                    }
                }
                result.setQuestionVos(questionVos);
                questionResults.add(result);
            }
        }
        paperVo.setCode(0);
        paperVo.setTimeDifferent(DateTimeUtil.getTimeDifference(paper.getStartTime(), paper.getEndTime()));
        paperVo.setPaperName(paper.getName());
        paperVo.setQuestionResults(questionResults);
        paperVo.setFullScore(getFullScore(num, score));
        model.addAttribute("result", paperVo);
        return "detail";
    }

    private Integer getFullScore(String[] paperTypeNum, String[] paperTypeScore) {
        Integer total = 0;
        for (int i = 0; i < paperTypeNum.length; i++) {
            total += Integer.parseInt(paperTypeNum[i]) * Integer.parseInt(paperTypeScore[i]);
        }
        return total;
    }

    @RequestMapping("/student/paper/submit")
    public String paperSubmit(HttpServletRequest request, Integer studentId, Integer paperId, String paperName, Integer fullScore) {
        Map<String, String[]> param = request.getParameterMap();
        Score commitScore = new Score();
        if (param.size() == Consts.Submit.BLANK_SUBMIT) {
            // 交白卷
            commitScore.setStudentScore(0);
        } else {
            // 判断试卷是否全是主观题
            Paper paper = paperService.getById(paperId);
            String[] type = paper.getType().split(",");
            boolean reuslt = "1".equals(type[3]);
            int score = 0;
            for (Map.Entry<String, String[]> entry : param.entrySet()) {
                if (entry.getKey().contains("paperId_")) {
                    String key = entry.getKey();
                    String[] values = entry.getValue();
                    Record record = new Record();
                    Integer questionId = Integer.parseInt(key.split("_")[1]);
                    record.setQuestionId(questionId);
                    record.setStudentId(studentId);
                    record.setPaperId(paperId);
                    record.setCreateTime(LocalDateTime.now());
                    record.setUpdateTime(LocalDateTime.now());
                    StringBuffer answerbuff = new StringBuffer();
                    String answer = "";
                    for (String v :
                            values) {
                        answerbuff.append(v + ",");
                    }

                    answer = answerbuff.substring(0, answerbuff.length() - 1);

                    record.setStudentAnswer(answer);
                    if (!reuslt) {
                        // 全是主观题 完成自动阅卷
                        Question question = questionService.getById(questionId);
                        String[] typeName = Consts.Question.TYPE_NAME;
                        String[] resultScore = paper.getScore().split(",");
                        int index = 0;
                        // 忽略大小写
                        if (answer.equalsIgnoreCase(question.getAnswer())) {
                            for (int i = 0; i < typeName.length; i++) {
                                if (question.getType().equals(typeName[i])) {
                                    index = i;
                                }
                            }
                            score += Integer.parseInt(resultScore[index]);
                        }
                        commitScore.setStudentScore(score);
                    }
                    recordService.save(record);
                }
            }

            if (!reuslt) {
                commitScore.setStudentId(studentId);
                commitScore.setPaperId(paperId);
                commitScore.setFullScore(fullScore);
                commitScore.setPaperName(paperName);
                commitScore.setCreateTime(DateTimeUtil.dateToStr(new Date()));
                commitScore.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
                scoreService.save(commitScore);
            }
        }

        return "redirect:/student/main";
    }

    @RequestMapping("/student/score/list")
    public String getScore(Model model) {
        Student student = (Student) UserContext.getUser();
        List<Score> scoreList = scoreService.list(new QueryWrapper<Score>().eq("student_id", student.getStudentId()));
        List<ScoreVo> scoreVos = new ArrayList<>();
        for (Score e :
                scoreList) {
            ScoreVo scoreVo = new ScoreVo();
            BeanUtils.copyProperties(e, scoreVo);
            scoreVo.setScore(e.getStudentScore());
            scoreVos.add(scoreVo);
        }
        model.addAttribute("student", student);
        model.addAttribute("scores", scoreVos);
        return "score";
    }

    @RequestMapping("/student/profile")
    public String profile(Model model) {
        Student student = (Student) UserContext.getUser();
        if (student == null) {
            return "index";
        }
        model.addAttribute("student", student);
        return "profile";
    }

    @RequestMapping("/student/profile/update")
    public String updateProfile(Student student) {
        if (student != null && student.getStudentId() != null) {
            if (StringUtils.isBlank(student.getPassword())) {
                student.setPassword(null);
            }
            boolean result = studentService.updateById(student);
            if (result) {
                return "redirect:/index";
            }
        }
        return "error/500";
    }

    @RequestMapping("/student/logOut")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request, Consts.Common.LOGIN_TOKEN);
        RedisPoolUtil.del(token);
        // 删除cookie
        CookieUtil.delLoginToken(request, response, Consts.Common.LOGIN_TOKEN);
        return "redirect:/index";
    }


    /*    *//**
     * 外部题库批量存储
     * @param args
     * @throws JSONException
     * @throws FileNotFoundException
     *//*
    @RequestMapping("/question/add")
    public void main(String[] args) throws JSONException, FileNotFoundException {
        String jsonStr = "";

        File file = ResourceUtils.getFile("classpath:json.txt");
        //绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(file);
             // 建立一个对象，它把文件内容转成计算机能读懂的语言
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                jsonStr += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(jsonStr);
        Question question = new Question();
        JSONObject list = object.getJSONObject("list");
        JSONArray examDtoList = list.getJSONArray("examDtoList");
        for (int i = 0; i < examDtoList.length(); i++) {
            JSONObject questionJson = (JSONObject) examDtoList.get(i);
            String answer = (String) questionJson.get("answer");
            String title = (String) questionJson.get("content");

            Integer exist = questionService.count(new QueryWrapper<Question>().eq("title", title));
            if (exist > 0) {
                continue;
            }
            String examTypeName = (String) questionJson.get("examTypeName");
            String optionA = "";
            String optionB = "";
            String optionC = "";
            String optionD = "";
            String optionE = "";

            JSONArray optionList = questionJson.getJSONArray("optionList");
            if (optionList.length() == 4) {
                optionA = (String) optionList.get(0);
                optionB = (String) optionList.get(1);
                optionC = (String) optionList.get(2);
                optionD = (String) optionList.get(3);
            } else {
                optionA = (String) optionList.get(0);
                optionB = (String) optionList.get(1);
                optionC = (String) optionList.get(2);
                optionD = (String) optionList.get(3);
                optionE = (String) optionList.get(4);
            }

            Question param = new Question();
            param.setTitle(title);
            param.setOptionA(optionA);
            param.setOptionB(optionB);
            param.setOptionC(optionC);
            param.setOptionD(optionD);
            param.setOptionE(optionE);
            param.setAnswer(answer);
            param.setType(examTypeName);
            param.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            param.setUpdateTime(DateTimeUtil.dateToStr(new Date()));

            questionService.save(param);

        }
        System.out.println(object.toString());

    }*/

}

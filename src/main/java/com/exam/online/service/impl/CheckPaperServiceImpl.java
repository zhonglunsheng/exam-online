package com.exam.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.online.common.Consts;
import com.exam.online.entity.*;
import com.exam.online.mapper.*;
import com.exam.online.service.CheckPaperService;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.vo.exam.QuestionResult;
import com.exam.online.vo.exam.QuestionVo;
import com.exam.online.vo.exam.StudentPaperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-04-22 16:58
 */
@Service
public class CheckPaperServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion> implements CheckPaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public StudentPaperVo getPaperDetail(Integer studentId, Integer paperId){
        StudentPaperVo studentPaperVo = new StudentPaperVo();
        studentPaperVo.setStudentId(studentId);
        studentPaperVo.setPaperId(paperId);

        List<QuestionResult> questionResultList = new ArrayList<>();

        // 补充逻辑 1、先获取试卷配置表里面的信息   2、按照顺序分别取出单项、多项、判断、简答、应用
        Paper paper = paperMapper.selectById(paperId);
        String[] paperTypeName = paper.getType().split(",");
        String[] paperTypeNums = paper.getTypeNums().split(",");
        String[] paperTypeScore = paper.getScore().split(",");
        String[] typeName = Consts.Question.TYPE_NAME;
        List<QuestionVo> questionVos;

        // 获取该学生考试的题库记录
        List<Record> recordList = recordMapper.selectList(new QueryWrapper<Record>()
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
            Score notJoinExamScore = new Score();
            notJoinExamScore.setPaperId(paperId);
            notJoinExamScore.setStudentId(studentId);
            notJoinExamScore.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            notJoinExamScore.setStudentScore(0);
            notJoinExamScore.setPaperName(paper.getName());
            notJoinExamScore.setFullScore(getFullScore(paperTypeNums, paperTypeScore));
            notJoinExamScore.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
            scoreMapper.insert(notJoinExamScore);
            return studentPaperVo;
        }

        QuestionResult questionResult;
        for (int i = 0; i < paperTypeName.length; i++) {
            questionResult = new QuestionResult();
            if ("1".equals(paperTypeName[i])) {
                questionResult.setName(typeName[i]);
                questionResult.setScore(Integer.parseInt(paperTypeScore[i]) * Integer.parseInt(paperTypeNums[i]));
                questionResult.setTotal(Integer.parseInt(paperTypeNums[i]));
                questionResult.setType(i);

                questionVos = new ArrayList<>();
                QuestionVo vo = new QuestionVo();

                List<Question> questions = questionMapper.selectList(new QueryWrapper<Question>()
                        .eq("type", typeName[i]).in("id", ids));

                for (Question n :
                        questions) {
                    Record record = recordMapper.selectOne(new QueryWrapper<Record>().eq("question_id", n.getId()).eq("paper_id",paperId));

                    vo.setTitle(n.getTitle());
                    vo.setOptionA(n.getOptionA() != null ?n.getOptionA().replace("&nbsp;", ""):n.getOptionA());
                    vo.setOptionB(n.getOptionB() != null ?n.getOptionB().replace("&nbsp;", ""):n.getOptionB());
                    vo.setOptionC(n.getOptionC() != null ?n.getOptionC().replace("&nbsp;", ""):n.getOptionC());
                    vo.setOptionD(n.getOptionD() != null ?n.getOptionD().replace("&nbsp;", ""):n.getOptionD());
                    vo.setOptionE(n.getOptionE() != null ?n.getOptionE().replace("&nbsp;", ""):n.getOptionE());

                    vo.setStudentAnswer(record.getStudentAnswer());
                    vo.setAnswer(n.getAnswer());
                    vo.setTargetScore(Integer.parseInt(paperTypeScore[i]));
                    vo.setSingleScore(n.getAnswer().equalsIgnoreCase(record.getStudentAnswer()) ? Integer.parseInt(paperTypeScore[i]) : 0);
                    questionVos.add(vo);
                }
                questionResult.setQuestionVos(questionVos);
                questionResultList.add(questionResult);
            }
        }
        studentPaperVo.setCode(0);
        studentPaperVo.setPaperName(paper.getName());
        studentPaperVo.setQuestionResults(questionResultList);
        studentPaperVo.setFullScore(getFullScore(paperTypeNums, paperTypeScore));
        return studentPaperVo;
    }

    private Integer getFullScore(String[] nums, String[] scores){
        Integer total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += Integer.parseInt(nums[i]) * Integer.parseInt(scores[i]);
        }

        return total;
    }
}

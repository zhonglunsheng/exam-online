package com.exam.online.vo.exam;

import com.exam.online.entity.Question;
import lombok.Data;

import java.util.Objects;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-20 14:35
 */
public class QuestionVo extends Question {
    private Integer id;
    /**
     * 学生答案
     */
    private String studentAnswer;

    /**
     * 得分情况
     */
    private Integer singleScore;

    /**
     * 单个题目满分
     */
    private Integer targetScore;


    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public Integer getSingleScore() {
        return singleScore;
    }

    public void setSingleScore(Integer singleScore) {
        this.singleScore = singleScore;
    }

    public Integer getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(Integer targetScore) {
        this.targetScore = targetScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        QuestionVo that = (QuestionVo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}

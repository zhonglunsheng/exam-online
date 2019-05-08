package com.exam.online.vo.exam;

import lombok.Data;

import java.util.List;

/**
 * @author zhonglunsheng
 * @Description 考试页面数据结构封装
 * @create 2019-02-20 14:27
 */
@Data
public class StudentPaperVo {
    private Integer code;
    private String msg;
    private String paperName;
    private Integer paperId;
    private Integer studentId;
    private Integer fullScore;
    private String timeDifferent;
    List<QuestionResult> questionResults;
}
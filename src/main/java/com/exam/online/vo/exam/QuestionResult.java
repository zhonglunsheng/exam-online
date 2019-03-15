package com.exam.online.vo.exam;

import lombok.Data;

import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-20 14:35
 */
@Data
public class QuestionResult {
    // 每个题型总分
    private Integer score;
    // 题型名称
    private String name;
    // 题型
    private Integer type;
    // 多少题
    private Integer total;
    private List<QuestionVo> questionVos;
}

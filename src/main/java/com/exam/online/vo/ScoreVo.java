package com.exam.online.vo;

import com.exam.online.entity.Student;
import lombok.Data;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-19 15:55
 */
@Data
public class ScoreVo extends StudentVo {
    private String paperName;
    private Integer score;
    /**
     * 满分
     */
    private Integer paperScore;
}

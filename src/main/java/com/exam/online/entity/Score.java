package com.exam.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生成绩表
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Score extends Model<Score> {

    private static final long serialVersionUID = 1L;

    /**
     * 学生成绩唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生id
     */
    private Integer studentId;

    /**
     * 试卷id
     */
    private Integer paperId;

    /**
     * 考试分数
     */
    private Integer studentScore;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    private String paperName;

    private Integer fullScore;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

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
 * 学生考试记录，教师阅卷表
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Record extends Model<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生id
     */
    private Integer studentId;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 试卷id
     */
    private Integer paperId;

    /**
     * 学生每题的答案
     */
    private String studentAnswer;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 参考答案
     */
    private String paperAnswer;

    /**
     * 是否阅卷成功 0：成功 1：未成功
     */
    private Integer check;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

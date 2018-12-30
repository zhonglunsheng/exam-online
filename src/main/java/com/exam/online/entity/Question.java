package com.exam.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 问题信息表:默认选择题4个选项A/B/C/D
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Question extends Model<Question> {

    private static final long serialVersionUID = 1L;

    /**
     * 问题唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 题型
     */
    private Integer type;

    /**
     * 选项A内容
     */
    @TableField("option_A")
    private String optionA;

    /**
     * 选项B内容
     */
    @TableField("option_B")
    private String optionB;

    /**
     * 选项C内容
     */
    @TableField("option_C")
    private String optionC;

    /**
     * 选项D内容
     */
    @TableField("option_D")
    private String optionD;

    /**
     * 状态 0：生效 1：失效
     */
    private Integer state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 难度
     */
    private Integer diff;

    /**
     * 正确答案
     */
    private String answer;

    /**
     * 分数
     */
    private Integer mark;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 添加额外选项
     */
    private Integer optionId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

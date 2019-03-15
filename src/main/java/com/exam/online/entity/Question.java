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
 * @since 2019-02-11
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
    private String type;

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
     * 选项F内容
     */
    @TableField("option_F")
    private String optionF;

    /**
     * 参考答案
     */
    private String answer;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 选项E内容
     */
    @TableField("option_E")
    private String optionE;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

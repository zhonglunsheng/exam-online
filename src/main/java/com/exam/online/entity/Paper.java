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
 * 试卷信息表
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Paper extends Model<Paper> {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态： 0：发布 1：结束
     */
    private Integer state;

    /**
     * 题目是否随机
     */
    private Boolean random;

    /**
     * 试卷包括的题型号 采用默认排列顺序
     */
    private String type;

    /**
     * 考试结束时间
     */
    private LocalDateTime endTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

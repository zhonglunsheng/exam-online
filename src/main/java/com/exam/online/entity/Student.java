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
 * 学生信息表
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Student extends Model<Student> {

    private static final long serialVersionUID = 1L;

    /**
     * 学生唯一标识
     */
    @TableId(value = "student_id", type = IdType.AUTO)
    private Integer studentId;

    /**
     * 学号
     */
    private String num;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 班级
     */
    private Integer classId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;


    @Override
    protected Serializable pkVal() {
        return this.studentId;
    }

}

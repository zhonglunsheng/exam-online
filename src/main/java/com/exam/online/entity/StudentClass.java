package com.exam.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 班级信息表
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentClass extends Model<StudentClass> {

    private static final long serialVersionUID = 1L;

    /**
     * 班级信息唯一标识
     */
    @TableId(value = "class_id", type = IdType.AUTO)
    private Integer classId;

    /**
     * 班级名称
     */
    private String className;


    @Override
    protected Serializable pkVal() {
        return this.classId;
    }

}

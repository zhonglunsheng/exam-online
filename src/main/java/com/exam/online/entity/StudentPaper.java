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
 * 学生参加考试信息表
 * </p>
 *
 * @author zhonglunsheng
 * @since 2018-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentPaper extends Model<StudentPaper> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 试卷id
     */
    private Integer paperId;

    /**
     * 学生id
     */
    private Integer studentId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

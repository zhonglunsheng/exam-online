package com.exam.online.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-14 16:11
 */
@Data
public class PageResult <T> implements Serializable {
    private Long total;
    private Integer code;
    private List<T> rows;

    private PageResult(Long total, Integer code, List<T> rows) {
        this.total = total;
        this.code = code;
        this.rows = rows;
    }

    public static PageResult success(Long total, List rows){
        return new PageResult(total, 0, rows);
    }
}

package com.exam.online.common;

import lombok.Data;

import java.util.List;

/**
 * @author zhonglunsheng
 * @Description Series数据结构封装
 * @create 2019-02-18 16:43
 */
@Data
public class Series {
    private String name;
    private List<Integer> data;
    private String type;
    private String stack;

    public Series() {
        this.type = "line";
        this.stack = "总量";
    }
}
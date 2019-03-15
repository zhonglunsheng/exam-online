package com.exam.online.common;

import lombok.Data;

import java.util.List;

//                type:'line',
//                        stack: '总量',
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
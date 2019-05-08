package com.exam.online.bo;

import lombok.Data;

import java.util.List;


/**
 * @author zhonglunsheng
 * @Description 图表返回json
 * @create 2019-02-18 16:43
 */
@Data
public class ChartsResult {

    private List<String> categories;
    private List<Series> series;
    private List<String> title;
}

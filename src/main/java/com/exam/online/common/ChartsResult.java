package com.exam.online.common;

import lombok.Data;

import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-18 16:43
 */

//{
//        "categories":[
//        "衬衫",
//        "羊毛衫"
//        ],
//        "series":[
//        {
//        "name": "销量2",
//        "data": [5,20]
//        },{
//        "name": "销量1",
//        "data": [50,20]
//        }
//        ],
//        "legend":{
//        "data":[
//        "销量1",
//        "销量2"
//        ]
//        }
//        }

/**
 * 图表返回json
  */
@Data
public class ChartsResult {

    private List<String> categories;
    private List<Series> series;
    private List<String> title;
}

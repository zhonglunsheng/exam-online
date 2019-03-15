package com.exam.online.vo;

import com.exam.online.entity.Paper;
import lombok.Data;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-15 17:54
 */
//
//         {
//         field: 'typeName',
//         title: '题型 ',
//         sortable: true
//         },
//         {
//         field: 'typeNums',
//         title: '题型数量 '
//         },
//         {
//         field: 'totalScore',
//         title: '总分 '
//         },

@Data
public class PaperVo extends Paper {
    private String typeName;
    private Integer totalScore;
    private String status;

}

package com.exam.online.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.common.Result;
import com.exam.online.entity.Paper;
import com.exam.online.service.PaperQuestionService;
import com.exam.online.service.PaperService;
import com.exam.online.service.QuestionService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷生成 管理
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@RestController
@RequestMapping("/admin/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    /**
     * 添加或修改试卷 名称 题目类型 类型下题目数量 是否随机
     * @return
     */
    @PostMapping("/save")
    public Result paperSave(Paper paper){
        if (paper == null || Strings.isBlank(paper.getName()) || paper.getStartTime() == null){
            return Result.error(Consts.COMOON.ARGUS_NULL);
        }
        paperService.saveOrUpdate(paper);
        return Result.success();
    }

    /**
     * 删除试卷
     * @param paperId
     * @return
     */
    @PostMapping("/remove")
    public Result paperSave(Integer paperId){
        if (paperId == null){
            return Result.error(Consts.PAPER.PAPER_ID_NULL);
        }
        Map<String,Object> inMap = new HashMap<>();
        inMap.put("paperId", paperId);
        List list = (List) paperQuestionService.listByMap(inMap);
        // 删除改试卷下的所有关联的题目
        if (list.size() > 0){
           paperQuestionService.removeByMap(inMap);
        }

        paperService.removeById(paperId);
        return Result.success();
    }

    /**
     * 查询试卷列表
     * @param page
     * @return
     */
    @RequestMapping("/find")
    public Result paperList(Page page, String name){
        if (page == null){
            page = new Page();
        }
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        if (Strings.isBlank(name)){
            queryWrapper.like("name", name);
        }
        return Result.success(paperService.page(page, queryWrapper));
    }

    /**
     * 试卷生成
     * @return
     */
    @RequestMapping("/random")
    public Result paperRandom(Integer paperId){
        if (paperId == null){
            return Result.error(Consts.COMOON.ARGUS_NULL);
        }
        return paperService.paperRandom(paperId);
    }

}


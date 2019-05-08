package com.exam.online.controller.backend;

import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.exam.online.service.PaperService;
import com.exam.online.service.QuestionService;
import com.exam.online.service.StudentService;
import com.exam.online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-19 14:41
 */
@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionService questionService;

    /**
     * 加载后台首页信息
     * @return
     */
    @GetMapping("/admin/list")
    @ResponseBody
    public Result list(){
        return Result.success(userService.getDataInfoCount());
    }
}

package com.exam.online.controller;

import com.exam.online.util.DateTimeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-03-15 15:14
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("/timeCheckStart")
    @ResponseBody
    public boolean checkStart(String start){
        Date dateNow = new Date();
        Date startTime = DateTimeUtil.strToDate(start);
        if (dateNow.getTime() - startTime.getTime() > 0){
            return false;
        }else{
            return true;
        }
    }

    @RequestMapping("/timeCheckEnd")
    @ResponseBody
    public boolean checkStart(String start, String end){
        Date endTime = DateTimeUtil.strToDate(end);
        Date startTime = DateTimeUtil.strToDate(start);
        if (endTime.getTime() - startTime.getTime() > 0){
            return true;
        }else{
            return false;
        }
    }
}

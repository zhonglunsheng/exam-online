package com.exam.online.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Result;
import com.exam.online.entity.Student;
import com.exam.online.service.StudentService;
import com.exam.online.vo.StudentVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生信息表 前端控制器
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    /**
     * 添加或修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value = "/s/save")
    public Result studentAdd(Student student){
        if (student != null){
            studentService.saveOrUpdate(student);
        }
        return Result.success();
    }

    /**
     * 删除学生信息
     * @param idList
     * @return
     */
    @PostMapping(value = "/s/remove")
    public Result studentDelete(@RequestBody List<Integer> idList){
        if (idList.size() != 0){
            studentService.removeByIds(idList);
        }
        return Result.success();
    }

    /**
     * 按条件分页查询全部学生列表
     * @param studentVo
     * @return
     */
    @GetMapping(value = "/s/find")
    public Result studentSelectByWrapper(@RequestBody StudentVo studentVo){
        QueryWrapper<Student> query = new QueryWrapper<>();
        Page<Student> page = studentVo.getPage();
        Map<String, Object> inMap = new HashMap<>();
        if (Strings.isBlank(studentVo.getName())){
            inMap.put("name", studentVo.getName());
        }

        if (Strings.isBlank(studentVo.getNum())){
            inMap.put("num", studentVo.getNum());
        }
        if (inMap != null && inMap.containsKey("name")){
            // 名字模糊查询
            String name = (String) inMap.remove("name");
            query.like("name", name);
        }else{
            // 其他条件一一匹配
            query.allEq(inMap);
        }
        return Result.success(studentService.pageMaps(page, query));
    }

}


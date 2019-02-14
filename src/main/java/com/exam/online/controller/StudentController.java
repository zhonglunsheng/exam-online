package com.exam.online.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.Student;
import com.exam.online.entity.StudentClass;
import com.exam.online.service.StudentService;
import com.exam.online.util.CommonUtil;
import com.exam.online.vo.StudentVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/admin/student")
    public String student(){
        return "backend/student/student";
    }

    @GetMapping("/admin/student/add")
    public String add(){
        return "backend/student/add";
    }

    @GetMapping("/admin/student/edit/{id}")
    public String add(@PathVariable("id") Integer id, Model model){
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "backend/student/edit";
    }

    /**
     * 获取班级列表
     * @param request
     * @return
     */
    @PostMapping("/admin/student/list")
    @ResponseBody
    public PageResult classList(HttpServletRequest request, Student student){
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        IPage<Student> page = null;
        if (pageSize != null && pageNum != null){
            page = new Page<Student>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        Integer total = 0;
        List<Student> students = studentService.list();
        if (students != null){
            total = students.size();
        }
        page = studentService.page(page, null);
        return PageResult.success(Long.valueOf(total), page.getRecords());
    }

    /**
     * 添加或修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value = "/admin/student/studentAdd")
    @ResponseBody
    public Result studentAdd(Student student){
        if (student != null){
            studentService.saveOrUpdate(student);
        }
        return Result.success();
    }

    /**
     * 删除学生信息
     * @param ids
     * @return
     */
    @PostMapping(value = "/admin/student/studentRemove")
    public Result studentRemove(String ids){
        boolean result = studentService.removeByIds(CommonUtil.StrToList(ids));
        if (result){
            return Result.success();
        }else{
            return Result.error();
        }
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


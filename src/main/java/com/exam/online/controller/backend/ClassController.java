package com.exam.online.controller.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.common.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.Student;
import com.exam.online.entity.StudentClass;
import com.exam.online.service.StudentClassService;
import com.exam.online.service.StudentService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-19 14:36
 */
@Controller
@RequestMapping("/admin/class")
public class ClassController {

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private StudentService studentService;
    /**
     * 跳转班级添加页面
     * @return
     */
    @GetMapping("/add")
    public String addClass(){
        return "/backend/class/add";
    }

    /**
     * 跳转班级修改页面
     * @return
     */
    @GetMapping("/edit/{id}")
    public String showClass(@PathVariable("id")Integer id, Model model){
        StudentClass studentClass = studentClassService.getById(id);
        model.addAttribute("class", studentClass);
        return "/backend/class/edit";
    }

    /**
     * 获取班级列表
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult classList(HttpServletRequest request, String className){
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        IPage<StudentClass> page = null;
        if (pageSize != null && pageNum != null){
            page = new Page<StudentClass>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        Integer total = 0;
        List<StudentClass> studentClasses = studentClassService.list();
        if (studentClasses != null){
            total = studentClasses.size();
        }
        page = studentClassService.page(page, null);
        return PageResult.success(Long.valueOf(total), page.getRecords());
    }

    /**
     * 添加班级信息
     * @param className
     * @param classId
     * @return
     */
    @PostMapping("/classAdd")
    @ResponseBody
    public Result classAdd(String className, Integer classId){
        StudentClass studentClass = new StudentClass();
        if (classId != null){
            studentClass.setClassId(classId);
        }
        studentClass.setClassName(className);
        studentClass.setCreateTime(DateTimeUtil.dateToStr(new Date()));
        studentClass.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        studentClassService.saveOrUpdate(studentClass);
        return Result.success();
    }

    /**
     * 跳转班级信息页面
     * @return
     */
    @GetMapping("/index")
    public String editClass(){
        return "/backend/class/class";
    }

    /**
     * 班级信息批量删除
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result classRemove(String ids){
        Integer count = studentService.count(new QueryWrapper<Student>().in("class_id", ids));
        if (count != 0){
            return Result.error("该班级下有学生不能删除");
        }
        studentClassService.removeByIds(CommonUtil.StrToList(ids));
        return Result.success(Consts.COMOON.SUCCESS);
    }
}

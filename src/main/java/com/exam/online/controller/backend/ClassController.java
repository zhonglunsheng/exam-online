package com.exam.online.controller.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.bo.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.Student;
import com.exam.online.entity.StudentClass;
import com.exam.online.service.StudentClassService;
import com.exam.online.service.StudentService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult classList(){
        Integer pageSize = ParamCheck.getParamNotNullForInteger("pageSize");
        Integer pageNum = ParamCheck.getParamNotNullForInteger("pageNum");
        IPage<StudentClass> page = new Page<>(pageNum, pageSize);
        List<StudentClass> studentClasses = studentClassService.list();
        page = studentClassService.page(page, null);
        return PageResult.success(ParamCheck.getListNotNullSize(studentClasses), page.getRecords());
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
        Boolean existClassName = ParamCheck.existRecord(studentClassService, "class_name", "className", null);
        if (existClassName){
            return Result.error("该班级名称已存在");
        }
        StudentClass studentClass = new StudentClass();
        if (classId != null){
            studentClass.setClassId(classId);
        }
        studentClass.setClassName(className);
        studentClass.setCreateTime(DateTimeUtil.dateToStr(new Date()));
        studentClass.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        studentClassService.saveOrUpdate(studentClass);
        return Result.success(Consts.Common.SUCCESS);
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
        studentClassService.removeByIds(CommonUtil.strToList(ids));
        return Result.success(Consts.Common.SUCCESS);
    }
}

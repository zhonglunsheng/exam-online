package com.exam.online.controller.backend;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.bo.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.Record;
import com.exam.online.entity.Score;
import com.exam.online.entity.Student;
import com.exam.online.entity.StudentClass;
import com.exam.online.service.RecordService;
import com.exam.online.service.ScoreService;
import com.exam.online.service.StudentClassService;
import com.exam.online.service.StudentService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.Md5Util;
import com.exam.online.util.ParamCheck;
import com.exam.online.vo.StudentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 学生信息管理
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Slf4j
@Controller
@RequestMapping("/admin/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RecordService recordService;



    @GetMapping("/")
    public String student(Model model){
        List<StudentClass> classList = studentClassService.list();
        model.addAttribute("classList", classList);
        return "backend/student/student";
    }

    @GetMapping("/add")
    public String add(Model model){
        List<StudentClass> classList = studentClassService.list();
        model.addAttribute("classList", classList);
        return "backend/student/add";
    }

    @GetMapping("/edit/{id}")
    public String add(@PathVariable("id") Integer id, Model model){
        Student student = studentService.getById(id);
        List<StudentClass> classList = studentClassService.list();
        model.addAttribute("student", student);
        model.addAttribute("classList", classList);
        return "backend/student/edit";
    }

    @GetMapping("/resetPwd/{id}")
    public String resetPwd(@PathVariable("id") Integer id, Model model){
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "backend/student/resetPwd";
    }

    /**
     * 获取学生列表
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult classList(HttpServletRequest request, Student student){
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        QueryWrapper queryWrapper= new QueryWrapper<Student>();
        IPage<Student> page = null;
        List<StudentVo> rows = new ArrayList<>();
        if (pageSize != null && pageNum != null){
            page = new Page<Student>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (student != null){
            if (!StringUtils.isBlank(student.getNum())){
                queryWrapper.like("num", student.getNum());
            }
            if (!StringUtils.isBlank(student.getName())){
                queryWrapper.like("name", student.getName());
            }
            if (student.getClassId()!=null){
                queryWrapper.eq("class_id",student.getClassId());
            }
        }
        // 按字段排序
        if (!StringUtils.isBlank(orderByColumn)&&!StringUtils.isBlank(isAsc)){
            if (Consts.Query.CREATE_TIME.equals(orderByColumn)) {
                orderByColumn = "create_time";
            }
            if (Consts.Query.ASC.equals(isAsc)) {
                queryWrapper.orderByAsc(orderByColumn);
            }else{
                queryWrapper.orderByDesc(orderByColumn);
            }
        }
        Integer total = 0;
        List<Student> students = studentService.list();
        if (students != null){
            total = students.size();
        }
        page = studentService.page(page, queryWrapper);
        List<Student> records = page.getRecords();
        StudentVo studentVo = null;
        if (records != null){
            for (Student s:
                    records) {
                studentVo = new StudentVo();
                BeanUtils.copyProperties(s, studentVo);
                Integer classId = s.getClassId();
                if (classId != null) {
                    StudentClass studentClass = studentClassService.getById(classId);
                    if (studentClass != null) {
                        studentVo.setClassName(studentClass.getClassName());
                    }
                }
                rows.add(studentVo);
            }
        }
        return PageResult.success(Long.valueOf(total), rows);
    }

    /**
     * 添加或修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value = "/studentAdd")
    @ResponseBody
    public Result studentAdd(Student student){
        Boolean existNum = ParamCheck.existRecord(studentService, "num", "num", null);
        if (existNum){
            return Result.error("该学号已存在");
        }
        if (student != null){
            if (student.getStudentId() == null){
                student.setPassword(Md5Util.md5Encodeutf8(student.getPassword()));
                student.setCreateTime(DateTimeUtil.dateToStr(new Date()));
                student.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
            }
            studentService.saveOrUpdate(student);
        }
        return Result.success();
    }

    /**
     * 删除学生信息
     * @param ids
     * @return
     */
    @PostMapping(value = "/remove")
    @ResponseBody
    public Result studentRemove(String ids){
        try{
            studentService.removeByIds(CommonUtil.strToList(ids));
            scoreService.remove(new QueryWrapper<Score>().in("student_id", CommonUtil.strToList(ids)));
            recordService.remove(new QueryWrapper<Record>().in("student_id", CommonUtil.strToList(ids)));
            return Result.success("删除成功");
        }catch (Exception e){
            log.error("删除学生出现异常{}", e);
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("删除失败");
        }
    }

    /**
     * 重置密码
     * @return
     */
    @PostMapping("/resetStudentPwd")
    @ResponseBody
    public Result resetStudentPwd(Integer studentId, String password){
        if (studentId == null || StringUtils.isBlank(password)){
            return Result.error(Consts.Common.ARGUS_NULL);
        }
        Student student = studentService.getById(studentId);
        student.setPassword(Md5Util.md5Encodeutf8(password));
        studentService.updateById(student);
        return Result.success();
    }
}


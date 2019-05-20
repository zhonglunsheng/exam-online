package com.exam.online.controller.backend;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.bo.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.Student;
import com.exam.online.entity.User;
import com.exam.online.service.UserService;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.Md5Util;
import com.exam.online.util.ParamCheck;
import com.exam.online.vo.TeacherVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 教师信息管理
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Controller
@RequestMapping("/admin/teacher")
public class TeacherController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String teacher(Model model){
        return "backend/teacher/teacher";
    }

    @GetMapping("/add")
    public String add(Model model){
        return "backend/teacher/add";
    }

    @GetMapping("/edit/{id}")
    public String add(@PathVariable("id") Integer id, Model model){
        User teacher = userService.getById(id);
        model.addAttribute("teacher", teacher);
        return "backend/teacher/edit";
    }

    @GetMapping("/resetPwd/{id}")
    public String resetPwd(@PathVariable("id") Integer id, Model model){
        User teacher = userService.getById(id);
        model.addAttribute("user", teacher);
        return "backend/teacher/resetPwd";
    }

    /**
     * 获取教师列表
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResult teacherList(HttpServletRequest request, User user){
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        QueryWrapper queryWrapper= new QueryWrapper<Student>();
        IPage<User> page = null;
        List<TeacherVo> rows = new ArrayList<>();
        if (pageSize != null && pageNum != null){
            page = new Page<User>(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
        // 根据条件查询
        if (user != null){
            if (!StringUtils.isBlank(user.getPhone())){
                queryWrapper.like("phone", user.getPhone());
            }
            if (!StringUtils.isBlank(user.getName())){
                queryWrapper.like("name", user.getName());
            }
        }
        // 按字段排序
        if (!StringUtils.isBlank(orderByColumn)&&!StringUtils.isBlank(isAsc)){
            if (Consts.Query.USER_ID.equals(orderByColumn)) {
                orderByColumn = "user_id";
            }
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
        page = userService.page(page, queryWrapper);
        List<User> teachers = page.getRecords();
        TeacherVo teacherVo = null;
        if (teachers != null){
            for (User u:
                    teachers) {
                teacherVo = new TeacherVo();
                BeanUtils.copyProperties(u, teacherVo);
                Integer classId = u.getRole();
                if (classId != null) {
                    if (classId == 0) {
                        teacherVo.setRoleName("管理员");
                    }
                    if (classId == 1) {
                        teacherVo.setRoleName("教师");
                    }
                }
                rows.add(teacherVo);
            }
        }
        if (teachers != null){
            total = teachers.size();
        }

        return PageResult.success(Long.valueOf(total), rows);
    }

    /**
     * 添加或修改教师信息
     * @param user
     * @return
     */
    @PostMapping(value = "/teacherAdd")
    @ResponseBody
    public Result teacherAdd(User user){
        Boolean existEmail = ParamCheck.existRecord(userService, "email", "email",null);
        if (existEmail){
            return Result.error("该邮箱已存在");
        }

        Boolean existPhone = ParamCheck.existRecord(userService, "phone", "email",null);
        if (existPhone){
            return Result.error("该手机号码已存在");
        }
        if (user != null){
            user.setPassword(Md5Util.md5Encodeutf8(user.getPassword()));
            user.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            user.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
            userService.saveOrUpdate(user);
        }
        return Result.success();
    }

    /**
     * 删除教师信息
     * @param ids
     * @return
     */
    @PostMapping(value = "/remove")
    @ResponseBody
    public Result teacherRemove(String ids){
        boolean result = userService.removeByIds(CommonUtil.strToList(ids));
        if (result){
            return Result.success();
        }else{
            return Result.error();
        }
    }

    /**
     * 重置密码
     * @return
     */
    @PostMapping("/resetTeacherPwd")
    @ResponseBody
    public Result resetStudentPwd(Integer userId, String password){
        if (userId == null || StringUtils.isBlank(password)){
            return Result.error(Consts.Common.ARGUS_NULL);
        }
        User teacher = userService.getById(userId);
        teacher.setPassword(Md5Util.md5Encodeutf8(password));
        userService.updateById(teacher);
        return Result.success();
    }
}


package com.exam.online.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.access.UserContext;
import com.exam.online.common.Consts;
import com.exam.online.common.PageResult;
import com.exam.online.common.Result;
import com.exam.online.entity.StudentClass;
import com.exam.online.entity.User;
import com.exam.online.service.*;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * <p>
 * 学生、教师管理
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-01-16
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private StudentClassService studentClassService;

    /**
     * 跳转登录页面
     * @return
     */
    @GetMapping("/system/login")
    public String doLogin(){
        return "login";
    }

    /**
     * 跳转后台首页
     * @param model
     * @return
     */
    @GetMapping("/user/main")
    public String toMain(Model model){
        User user = UserContext.getUser();
        if (user == null) user = new User();
        if (user.getAvatar() == null){
            user.setAvatar("/avatar/default.jpg");
        }
        model.addAttribute("user", user);
        return "backend/main";
    }

    /**
     * 跳转个人信息
     * @return
     */
    @GetMapping("/user/profile")
    public String profile(Model model){
        User user = UserContext.getUser();
        model.addAttribute("user", user);
        return "backend/user/profile/profile";
    }

    /**
     * 跳转个人信息修改
     * @return
     */
    @GetMapping("/user/profile/edit")
    public String profileEdit(Model model){
        User user = UserContext.getUser();
        model.addAttribute("user", user);
        return "backend/user/profile/edit";
    }

    /**
     * 挑战修改密码页面
     * @return
     */
    @GetMapping("/user/profile/resetPwd")
    public String profileResetPwd(Model model){
        User user = UserContext.getUser();
        model.addAttribute("user", user);
        return "backend/user/profile/resetPwd";
    }


    /**
     * 跳转班级添加页面
     * @return
     */
    @GetMapping("/admin/class/add")
    public String addClass(){
        return "/backend/class/add";
    }

    /**
     * 跳转班级信息页面
     * @return
     */
    @GetMapping("/admin/class")
    public String editClass(){
        return "/backend/class/class";
    }

    /**
     * 跳转班级修改页面
     * @return
     */
    @GetMapping("/admin/class/edit/{id}")
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
    @PostMapping("/admin/class/list")
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
    @PostMapping("/admin/class/classAdd")
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
     * 班级信息批量删除
     * @param ids
     * @return
     */
    @PostMapping("/admin/class/remove")
    @ResponseBody
    public Result classRemove(String ids){
        studentClassService.removeByIds(CommonUtil.StrToList(ids));
        return Result.success();
    }

    /**
     * 管理员登陆
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/login")
    @ResponseBody
    public Result login(User user, HttpServletRequest request){
        //账号和密码是否存在
        String email = user.getEmail();
        String password = user.getPassword();

        if (Strings.isBlank(email) || Strings.isBlank(password)){
            return Result.error(Consts.LOGIN.LOGIN_NULL);
        }
        Result result = userService.login(email, MD5Util.MD5EncodeUtf8(password));
        request.getSession().setAttribute("user", result.getData());
        return result;
    }

    /**
     * 加载后台首页信息
     * @return
     */
    @GetMapping("/admin/list")
    @ResponseBody
    public Result list(){
      List<Integer> result = new ArrayList<>();
      List<User> users = userService.list();
      int count = 0;
      if (users == null || users.size() == 0){
          result.add(0);
          result.add(0);
      }else{
          for (User user:
                  users) {
              if (user != null){
                count = user.getRole() == 0? count + 1:count;
              }
          }
          result.add(count);
          result.add(users.size() - count);
      }
      result.add(studentService.list().size());
      result.add(paperService.list().size());
      result.add(questionService.list().size());
      return Result.success(result);
    }
    /**
     * 检查密码
     * @return
     */
    @GetMapping("/user/profile/checkPassword")
    @ResponseBody
    public boolean checkPassword(Model model,String password){
        User user = UserContext.getUser();
        if (MD5Util.MD5EncodeUtf8(password).equals(user.getPassword())){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 个人信息修改
     * @return
     */
    @PostMapping("/user/profile/update")
    @ResponseBody
    public Result profileUpdate(User user, HttpSession session){
        boolean result = userService.updateById(user);
        if (result){
            user = userService.getById(user.getUserId());
            session.setAttribute("user", user);
        }
        return Result.success();
    }

    @PostMapping("/user/profile/updatePwd")
    @ResponseBody
    public Result profileUpdatePwd(String password, HttpSession session){
        User user = UserContext.getUser();
        user.setPassword(MD5Util.MD5EncodeUtf8(password));
        boolean result = userService.updateById(user);
        if (result){
            session.setAttribute("user", user);
            return Result.success();
        }else{
            return Result.error();
        }
    }

    /**
     * 添加或修改教师信息
     * @param teacher
     * @return
     */
    @PostMapping(value = "/t/save")
    public Result teacherAdd(@RequestBody User teacher){
        if (teacher != null){
            userService.saveOrUpdate(teacher);
        }
        return Result.success();
    }

    /**
     * 删除教师信息
     * @param idList
     * @return
     */
    @PostMapping(value = "/t/remove")
    public Result teacherDelete(@RequestBody List<Integer> idList){
        if (idList.size() != 0){
            studentService.removeByIds(idList);
        }
        return Result.success();
    }

    /**
     * 按条件分页查询全部教师列表
     * @param page
     * @param name
     * @return
     */
    @GetMapping(value = "/t/find")
    public Result teacherSelectByWrapper(@RequestBody IPage<User> page, String name){
        QueryWrapper<User> query = new QueryWrapper<>();
        Map<String, Object> inMap = new HashMap<>();
        if (Strings.isBlank(name)){
            query.like("name", name);
        }
        return Result.success(userService.pageMaps(page, query));
    }

}


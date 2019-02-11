package com.exam.online.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.online.common.Consts;
import com.exam.online.common.Result;
import com.exam.online.entity.Student;
import com.exam.online.entity.User;
import com.exam.online.service.PaperService;
import com.exam.online.service.QuestionService;
import com.exam.online.service.StudentService;
import com.exam.online.service.UserService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/admin")
    public String doLogin(){
        return "login";
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
     * 跳转后台首页
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/user/main")
    public String toMain(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) user = new User();
        if (user.getAvatar() == null){
            user.setAvatar("/avatar/default.jpg");
        }
        model.addAttribute("user", user);
        return "backend/main";
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
    public Result teacherSelectByWrapper(@RequestBody Page<User> page, String name){
        QueryWrapper<User> query = new QueryWrapper<>();
        Map<String, Object> inMap = new HashMap<>();
        if (Strings.isBlank(name)){
            query.like("name", name);
        }
        return Result.success(userService.pageMaps(page, query));
    }

}


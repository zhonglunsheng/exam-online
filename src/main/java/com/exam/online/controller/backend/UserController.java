package com.exam.online.controller.backend;

import com.exam.online.access.UserContext;
import com.exam.online.common.Consts;
import com.exam.online.common.Result;
import com.exam.online.entity.User;
import com.exam.online.service.*;
import com.exam.online.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 后台信息管理
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

    /**
     * 跳转登录页面
     * @return
     */
    @GetMapping("/system/login")
    public String doLogin(){
        return "login";
    }

    /**
     * 后台登陆
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/system/login")
    @ResponseBody
    public Result login(User user, HttpServletRequest request){
        //账号和密码是否存在
        String email = user.getEmail();
        String password = user.getPassword();

        if (Strings.isBlank(email) || Strings.isBlank(password)){
            return Result.error(Consts.Login.LOGIN_NULL);
        }
        Result result = userService.login(email, Md5Util.md5Encodeutf8(password));
        request.getSession().setAttribute("user", result.getData());
        return result;
    }

    /**
     * 安全退出
     * @param request
     * @return
     */
    @RequestMapping(value = "/system/logOut")
    public String logOut(HttpServletRequest request){
      HttpSession session = request.getSession();
      session.invalidate();
      return "redirect:/system/login";
    }

    /**
     * 跳转后台首页
     * @param model
     * @return
     */
    @GetMapping("/user/main")
    public String toMain(Model model){
        User user = UserContext.getUser();
        if (user == null) {
            user = new User();
        }
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
    @GetMapping("/user/profile/edit/{userId}")
    public String profileEdit(@PathVariable("userId") Integer userId, Model model){
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "backend/user/profile/edit";
    }

    /**
     * 挑战修改密码页面
     * @return
     */
    @GetMapping("/user/profile/resetPwd/{userId}")
    public String profileResetPwd(@PathVariable("userId") Integer userId, Model model){
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "backend/user/profile/resetPwd";
    }
    /**
     * 检查密码
     * @return
     */
    @GetMapping("/user/profile/checkPassword")
    @ResponseBody
    public boolean checkPassword(Model model,String password){
        User user = UserContext.getUser();
        if (Md5Util.md5Encodeutf8(password).equals(user.getPassword())) {
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
        user.setPassword(Md5Util.md5Encodeutf8(password));
        boolean result = userService.updateById(user);
        if (result){
            session.setAttribute("user", user);
            return Result.success();
        }else{
            return Result.error();
        }
    }

}


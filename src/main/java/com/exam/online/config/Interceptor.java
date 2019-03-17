package com.exam.online.config;

import com.exam.online.access.UserContext;
import com.exam.online.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-01-16 17:20
 */
@Slf4j
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null){
            UserContext.setUser(user);
        }
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry entry:
             map.entrySet()) {
            log.info("请求参数 "+entry.getKey()+":"+ ((String[])entry.getValue())[0]);
        }

        if (user != null){
            UserContext.setUser(user);
            return true;
        }else{
            response.sendRedirect("/system/login");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}

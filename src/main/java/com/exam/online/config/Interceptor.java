package com.exam.online.config;

import com.exam.online.common.Result;
import com.exam.online.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

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
//        Map<String, String[]> map = request.getParameterMap();
//        for (Map.Entry entry:
//             map.entrySet()) {
//            log.info("请求参数 "+entry.getKey()+":"+ ((String[])entry.getValue())[0]);
//        }
//        // 判断是否已经登录
//        HttpSession session = request.getSession();
//        // 根据请求URL进行权限判定
//        String url = request.getRequestURL().toString();
//        User user = null;
//        if (url.contains("admin")){
//            user = (User) session.getAttribute("admin");
//        }else{
//            user = (User) session.getAttribute("teacher");
//        }
//
//        if (user != null){
//            return true;
//        }else{
//            //返回错误
//            response.reset();
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json;charset=UTF-8");
//
//            PrintWriter out = response.getWriter();
//            out.print("请先登录系统");
//            out.flush();
//            out.close();
//
//            return false;
//        }

        return true;
    }
}

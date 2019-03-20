package com.exam.online.config;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.exam.online.access.UserContext;
import com.exam.online.common.Consts;
import com.exam.online.entity.Student;
import com.exam.online.entity.User;
import com.exam.online.util.CookieUtil;
import com.exam.online.util.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry entry :
                map.entrySet()) {
            log.info("请求参数 " + entry.getKey() + ":" + ((String[]) entry.getValue())[0]);
        }
        String url = request.getRequestURL().toString();

        if (url.contains("/admin") || url.contains("/user")) {
            String loginToken = CookieUtil.readLoginToken(request, Consts.Common.USER_TOKEN);
            String token = RedisPoolUtil.get(loginToken);
            if (loginToken == null) {
                response.sendRedirect("/system/login");
                return false;
            }
            User user = JSONObject.parseObject(token, User.class);
            if (user != null) {
                UserContext.setUser(user);
                return true;
            }
        } else {
            String loginToken = CookieUtil.readLoginToken(request, Consts.Common.STUDENT_TOKEN);
            String token = RedisPoolUtil.get(loginToken);
            if (loginToken == null) {
                response.sendRedirect("/index");
                return false;
            }
            Student student = JSONObject.parseObject(token, Student.class);
            if (student != null) {
                UserContext.setUser(student);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}

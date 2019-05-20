package com.exam.online.util;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description 参数校验判断
 * @create 2019-04-22 15:15
 */
@Slf4j
public class ParamCheck {

    /**
     * 参数非空校验 返回String
     * @param paramKey
     * @return
     */
    public static String getParamNotNullForString(String paramKey){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String paramValue = request.getParameter(paramKey);
        if (StringUtils.isBlank(paramValue)){
            log.error("必填参数：{} 为空", paramKey);
            throw new RuntimeException();
        }else{
            return paramValue;
        }
    }

    /**
     * 参数非空校验 返回Integer
     * @param paramKey
     * @return
     */
    public static Integer getParamNotNullForInteger(String paramKey){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String paramValue = request.getParameter(paramKey);
        if (StringUtils.isBlank(paramValue)){
            log.error("必填参数：{} 为空", paramKey);
            throw new RuntimeException();
        }else{
            return Integer.parseInt(paramValue);
        }
    }

    /**
     * 参数非空校验 返回Integer
     * @param paramKey
     * @return
     */
    public static Object getParamNotNullForObject(String paramKey){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Object paramValue = request.getAttribute(paramKey);
        if (paramKey == null){
            log.error("必填参数：{} 为空", paramKey);
            throw new RuntimeException();
        }else{
            return paramValue;
        }
    }

    public static Long getListNotNullSize(List list){
        if (list != null){
            return (long) list.size();
        }else{
            return 0L;
        }
    }

    /**
     * 判断是否数据库已存在记录
     * @param iService
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> Boolean existRecord(IService<T> iService, String column, String key, String value){
        if (value == null){
            value = ParamCheck.getParamNotNullForString(key);
        }
        return iService.count((Wrapper<T>) new QueryWrapper().eq(column, value)) > 0;
    }

}

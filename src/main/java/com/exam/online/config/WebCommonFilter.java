package com.exam.online.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exam.online.common.Consts;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-04-22 16:00
 */
@javax.servlet.annotation.WebFilter(urlPatterns = "/*", filterName = "webCommonFilter")
public class WebCommonFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String orderByColumn = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");
        // 查询封装
        setQueryWrapperByOrder(request, orderByColumn, isAsc);
        chain.doFilter(request, response);
    }

    private void setQueryWrapperByOrder(ServletRequest request, String orderByColumn, String isAsc) {
        QueryWrapper queryWrapper = null;
        // 按字段排序
        if (!StringUtils.isBlank(orderByColumn) && !StringUtils.isBlank(isAsc)) {
            queryWrapper = new QueryWrapper<>();
            if (Consts.Query.CREATE_TIME.equals(orderByColumn)) {
                orderByColumn = "create_time";
            }
            if (Consts.Query.ASC.equals(isAsc)) {
                queryWrapper.orderByAsc(orderByColumn);
            } else {
                queryWrapper.orderByDesc(orderByColumn);
            }
        }
        request.setAttribute("queryWrapper", queryWrapper);
    }

    @Override
    public void destroy() {

    }
}

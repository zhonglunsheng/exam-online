package com.exam.online.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-01-16 17:25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor()).addPathPatterns("/user/**");
    }

    /**
     * CORS跨域请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //拦截所有的url
        registry.addMapping("/**")
                // 放行哪些原始域，比如"http://domain1.com,https://domain2.com"
                .allowedOrigins("*")
                // 是否发送Cookie信息
                .allowCredentials(true)
                // 放行哪些原始域(请求方式)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 放行哪些原始域(头部信息)
                .allowedHeaders("*");
    }
}

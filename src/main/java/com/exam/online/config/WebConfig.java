package com.exam.online.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-01-16 17:25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**");
    }

    /**
     * CORS跨域请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//拦截所有的url
                .allowedOrigins("*")// 放行哪些原始域，比如"http://domain1.com,https://domain2.com"
                .allowCredentials(true)// 是否发送Cookie信息
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 放行哪些原始域(请求方式)
                .allowedHeaders("*");// 放行哪些原始域(头部信息)
    }
}

package com.MyDisk.project.config;

import com.MyDisk.project.controller.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * @Description  注册登录拦截器 addPathPatterns() -> 拦截的请求  excludePathPatterns -> 不拦截的请求
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }

}

package com.zazhi.config;

import com.zazhi.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //登录接口和注册接口放行
//        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login", "/user/register", "/doc.html");
//    }

}

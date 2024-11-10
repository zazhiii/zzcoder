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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对swagger、登录注册的请求不进行拦截
        String[] excludePatterns = new String[]{
                // knife4j 的请求
                "/swagger-resources/**",
                "/webjars/**",
                "/v3/**",
                "/swagger-ui.html/**",
                "/api",
                "/api-docs",
                "/api-docs/**",
                "/doc.html/**",
                // 本项目的请求
                "/api/register",
                "/api/login",
                "/api/login-by-email-code",
                "/api/send-email-verification-code",
                "/api/update-password-by-email",
                "/api/problem/list",
                "/api/problem/{id}",
                "/api/tag",
        };

        //登录接口和注册接口放行
        registry.addInterceptor(loginInterceptor).excludePathPatterns(excludePatterns);
    }

}

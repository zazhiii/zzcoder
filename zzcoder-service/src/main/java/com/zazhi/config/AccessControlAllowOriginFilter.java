package com.zazhi.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
 
@Configuration
@Slf4j
public class AccessControlAllowOriginFilter implements WebMvcConfigurer {
 
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        log.info("允许跨域配置......");
//
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:7070") // 允许特定来源
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
//                .allowedHeaders("*") // 允许的请求头
//                .exposedHeaders("*") // 允许暴露的响应头
//                .allowCredentials(true) // 允许携带凭证
//                .maxAge(3600); // 预检请求的缓存时间
//    }
}
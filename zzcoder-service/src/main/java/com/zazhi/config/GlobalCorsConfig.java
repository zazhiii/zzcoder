package com.zazhi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Slf4j
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        log.info("允许跨域配置......");
        CorsConfiguration config = new CorsConfiguration();
        
        // 设置允许的源，比如允许 http://localhost:7070 访问
        config.addAllowedOrigin("http://localhost:7070");
        
        // 设置是否允许发送Cookie
        config.setAllowCredentials(true);
        
        // 设置允许的请求方法，如 GET, POST 等
        config.addAllowedMethod("*");
        
        // 设置允许的头部信息
        config.addAllowedHeader("*");
        
        // 设置暴露的头部信息
        config.addExposedHeader("Authorization");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

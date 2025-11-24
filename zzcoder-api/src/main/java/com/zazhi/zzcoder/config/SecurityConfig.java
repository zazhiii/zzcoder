package com.zazhi.zzcoder.config;

import com.zazhi.zzcoder.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author lixh
 * @since 2025/9/9 12:13
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        // 创建一个用户认证提供者
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置用户相信信息，可以从数据库中读取、或者缓存、或者配置文件
        authProvider.setUserDetailsService(userDetailsService);
        // 设置加密机制，若想要尝试对用户进行身份验证，我们需要知道使用的是什么编码
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //禁用csrf(防止跨站请求伪造攻击)
                .csrf(CsrfConfigurer::disable)
                // 设置白名单
                .authorizeHttpRequests(
                        registry -> registry
                                // 登录 -> 允许所有人访问
                                .requestMatchers("/api/login").permitAll()
                                // knife4j -> 可匿名访问
                                .requestMatchers("/doc.html").anonymous()
                                .requestMatchers("/webjars/**").anonymous()
                                .requestMatchers("/v3/**").anonymous()
                                .requestMatchers("/swagger-resources/**").anonymous()
                                // 其余接口 -> 需要认证
                                .anyRequest().authenticated()
                )
                // 禁用 session
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .authenticationProvider(authenticationProvider)
                // 添加JWT过滤器
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 登出操作
                .logout(logout -> logout
                                .logoutUrl("/api/logout")
//                        .addLogoutHandler()
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()))
                .build();
    }
}
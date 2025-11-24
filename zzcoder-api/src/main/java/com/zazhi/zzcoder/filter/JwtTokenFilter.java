package com.zazhi.zzcoder.filter;

import com.zazhi.zzcoder.common.constants.RedisKey;
import com.zazhi.zzcoder.common.utils.JwtUtil;
import com.zazhi.zzcoder.common.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author lixh
 * @since 2025/9/9 21:20
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String jwtToken = request.getHeader("Authorization");
        // 如果没有token，直接放行
        if(!StringUtils.hasText(jwtToken)){
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        Map<String, Object> map;
        try {
            map = JwtUtil.parseToken(jwtToken);
        } catch (Exception e) {
            throw new RuntimeException("token无效");
        }
        Integer userId = (Integer) map.get("userId");

        // 校验redis中是否存在
        String key = RedisKey.format(RedisKey.LOGIN, userId);
        UserDetails userDetails = redisUtil.getObject(key, UserDetails.class);
        if(userDetails == null){
            throw new RuntimeException("用户未登录");
        }

        // 这样创建出来的token是已认证状态
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);

        // 继续
        filterChain.doFilter(request, response);
    }
}

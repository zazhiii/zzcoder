package com.zazhi.interceptor;

import com.zazhi.utils.JwtUtil;
import com.zazhi.utils.RedisUtil;
import com.zazhi.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        try {
            // 判断从redis中是否能获取到这个token
            String getToken = redisUtil.get(token);
            if(getToken == null){
                throw new RuntimeException();
            }

            // 解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);

            //把业务数据存到ThreadLocal
            claims.put("id", ((Number)claims.get("id")).longValue());
            ThreadLocalUtil.set(claims);
            //放行
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            //不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}

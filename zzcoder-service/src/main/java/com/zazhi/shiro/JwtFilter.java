package com.zazhi.shiro;

import com.zazhi.shiro.JwtToken;
import com.zazhi.utils.ThreadLocalUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Component
public class JwtFilter extends AuthenticatingFilter {

    // 拦截请求之后，用于把令牌字符串封装成令牌对象
    // 该方法用于从请求中获取 JWT 并将其封装为 JwtToken（自定义的 AuthenticationToken 类）。
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String jwtToken = httpRequest.getHeader("Authorization");
        if (!StringUtils.hasLength(jwtToken)) {
            return null;
        }
        return new JwtToken(jwtToken);
    }

    // 该方法的作用是判断当前请求是否被允许访问。它主要用于检查某些条件，决定是否允许访问或是否跳过认证过程
    // 他作用于 onAccessDenied 之前
    // 如果请求满足某些条件（例如，isAccessAllowed 返回 true），Shiro 会跳过后续的认证步骤，允许请求继续。
    // 如果返回 false，Shiro 会继续执行 onAccessDenied，进行认证或其他授权操作。
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 从请求头中获取 Token
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String jwtToken = httpRequest.getHeader("Authorization");

        if (StringUtils.hasLength(jwtToken)) { // 若当前请求存在 Token，则执行登录操作
            try {
                log.debug("请求路径 {} 开始认证, token: {}", httpRequest.getRequestURI(), jwtToken);
//                getSubject(request, response).login(new JwtToken(jwtToken));
                executeLogin(request, response);
                log.debug("{} 认证成功", httpRequest.getRequestURI());
                return true;
            } catch (Exception e) {
                log.error("{} 认证失败", httpRequest.getRequestURI());
            }
        }
        // 若当前请求不存在 Token，没有认证意愿，直接放行
        // 例如，登录接口或者游客可访问的接口不需要 Token
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            // 返回true则继续执行拦截链，返回false则中断后续拦截，直接返回，option请求显然无需继续判断，直接返回
            return false;
        }
        return super.preHandle(request, response);
    }

    // 该方法用于在请求处理完成后执行清理工作
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        ThreadLocalUtil.remove();
    }
}

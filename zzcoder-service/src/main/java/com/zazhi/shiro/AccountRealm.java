package com.zazhi.shiro;

import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import com.zazhi.entity.User;
import com.zazhi.service.UserService;
import com.zazhi.utils.JwtUtil;
import com.zazhi.utils.RedisUtil;
import com.zazhi.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 这个方法用于判断 AccountRealm 是否支持该类型的 Token。
     * 如果是 JwtToken，则返回 true，表示当前 Realm 支持该 Token 类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Long userId = (Long) principals.getPrimaryPrincipal();
        // 这里查询数据库获取用户的角色和权限
        List<Role> roles = userService.getUserRolesById(userId);
        List<Permission> permissions = userService.getUserPermissionsByRoles(roles);

        // 将角色和权限名称加入到 Set 集合中
        Set<String> roleNames = new HashSet<>();
        Set<String> permissionNames = new HashSet<>();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        for (Permission permission : permissions) {
            permissionNames.add(permission.getName());
        }

        // 创建 SimpleAuthorizationInfo 对象
        // 并使用 addRoles 和 addStringPermissions 方法将角色和权限添加到授权信息中。
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleNames);
        authorizationInfo.setStringPermissions(permissionNames);
        return authorizationInfo;
    }

    /**
     * 用于验证用户身份
     *
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        JwtToken token = (JwtToken) authenticationToken;
        String jwtToken = (String) token.getPrincipal();

        // 判断 token 是否过期
        if (redisUtil.get(jwtToken) == null) {
            throw new AuthenticationException("token已过期，请重新登录");
        }
        // 解析 token 是否合法
        Map<String, Object> map;
        try {
            map = JwtUtil.parseToken(jwtToken);
        } catch (Exception e) {
            throw new AuthenticationException("token非法，可能被篡改");
        }
        // 将用户信息存入 ThreadLocal
        ThreadLocalUtil.set(map);
        log.info(map.toString());
        // 获取用户 id
        Long userId = Long.valueOf(map.get("id").toString());
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        return new SimpleAuthenticationInfo(userId, jwtToken, getName());
    }
}

package com.zazhi.shiro;

import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.service.UserService;
import com.zazhi.common.utils.JwtUtil;
import com.zazhi.common.utils.RedisUtil;
import com.zazhi.common.utils.ThreadLocalUtil;
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

import static com.zazhi.common.constant.RedisKeyConstants.JWT_TOKEN;

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
        Integer userId = (Integer) principals.getPrimaryPrincipal();
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

        // 解析 token 是否合法
        Map<String, Object> map;
        try {
            map = JwtUtil.parseToken(jwtToken);
        } catch (Exception e) {
            throw new AuthenticationException("token非法，可能被篡改");
        }

        // 判断 token 是否过期
        String key = JWT_TOKEN + map.get("id");
        if (redisUtil.get(key) == null) {
            throw new AuthenticationException("token已过期，请重新登录");
        }

        // 将用户信息存入 ThreadLocal
        ThreadLocalUtil.set(map);
        log.info(map.toString());
        // 获取用户 id
        Integer userId = Integer.valueOf(map.get("id").toString());
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        return new SimpleAuthenticationInfo(userId, jwtToken, getName());
    }
}

package com.zazhi.zzcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zazhi.zzcoder.common.pojo.entity.User;
import com.zazhi.zzcoder.mapper.UserMapper;
import com.zazhi.zzcoder.security.LoginUserDetails;
import com.zazhi.zzcoder.security.RoleAndPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author lixh
 * @since 2025/9/9 11:56
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, identification));
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, identification));
        }
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, identification));
        }
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 获取用户的角色和权限
        RoleAndPermission rp = userMapper.getRoleAndPermission(user.getId());
        return new LoginUserDetails(user, rp.getRoles(), rp.getPermissions());
    }
}

package com.zazhi.initializer;

import cn.hutool.crypto.digest.MD5;
import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.mapper.AuthMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.common.pojo.properties.AdminProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final AuthMapper authMapper;

    private final UserMapper userMapper;

    private final AdminProperties adminProp;

    @Override
    @Transactional
    public void run(ApplicationArguments args){
        String adminRoleName = adminProp.getRoleName();
        String adminUsername = adminProp.getUsername();
        String adminPassword = adminProp.getPassword();
        String adminEmail = adminProp.getEmail();

        Role role = authMapper.getRoleByName(adminRoleName);
        User user = userMapper.getByName(adminUsername);

        if(user == null){
            user = User.builder()
                    .username(adminUsername)
                    .password(MD5.create().digestHex(adminPassword))
                    .email(adminEmail)
                    .build();
            userMapper.insert(user);
        }

        if(role == null){
            role = Role.builder()
                    .name(adminRoleName)
                    .description("超级管理员")
                    .build();
            authMapper.addRole(role);
        }

        // 添加所有权限到超级管理员角色
        List<Integer> already = authMapper.getPermissionsByRoleId(role.getId()).stream().map(Permission::getId).toList();
        Integer RoleId = role.getId();
        authMapper.getAllPermissions().forEach(permission -> {
            if(!already.contains(permission.getId())){
                authMapper.addPermissionToRole(RoleId, permission.getId());
            }
        });

        // 关联角色和用户
        if(!authMapper.userHasRole(user.getId(), RoleId)){
            authMapper.addRoleToUser(role.getId(), user.getId());
        }
        log.info("""
                            超级管理员账号初始化成功
                            账号：{}
                            密码：{}""",
                adminUsername,
                adminPassword);
    }
}

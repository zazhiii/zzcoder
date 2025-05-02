package com.zazhi.initializer;

import cn.hutool.crypto.digest.MD5;
import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import com.zazhi.entity.User;
import com.zazhi.mapper.AuthMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.properties.AdminProperties;
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

        if(role != null && user != null){
            log.info("""
                            超级管理员账号已存在
                            账号：{}
                            密码：{}""",
                    adminUsername,
                    adminPassword);
            return;
        }

        if(role == null){
            role = Role.builder()
                    .name(adminRoleName)
                    .description("超级管理员")
                    .build();
            authMapper.addRole(role);
        }

        if(user == null){
            user = User.builder()
                    .username(adminUsername)
                    .password(MD5.create().digestHex(adminPassword))
                    .email(adminEmail)
                    .build();
            userMapper.insert(user);
        }

        // 添加所有权限添加到角色
        List<Integer> already = authMapper.getPermissionsByRoleId(role.getId()).stream().map(Permission::getId).toList();
        Integer adminRoleId = role.getId();
        authMapper.getAllPermissions().forEach(permission -> {
            if(!already.contains(permission.getId())){
                authMapper.addPermissionToRole(adminRoleId, permission.getId());
            }
        });

        // 关联角色和用户
        if(!authMapper.userHasRole(user.getId(), adminRoleId)){
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

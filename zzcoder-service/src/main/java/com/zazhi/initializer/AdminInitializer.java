package com.zazhi.initializer;

import cn.hutool.crypto.digest.MD5;
import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import com.zazhi.entity.User;
import com.zazhi.mapper.AuthMapper;
import com.zazhi.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void run(ApplicationArguments args){
        Role role = authMapper.getRoleByName("super-idol"); // TODO 外部化配置
        User user = userMapper.getByName("super-idol");

        if(role != null && user != null){
            log.info("超级管理员账号已存在, 无需初始化");
            return;
        }

        if(role == null){
            role = Role.builder()
                    .name("super-idol")
                    .description("超级管理员")
                    .build();
            authMapper.addRole(role);
        }

        if(user == null){
            user = User.builder()
                    .username("super-idol")
                    .password(MD5.create().digestHex("super-idol"))
                    .email("xxxxxxxxx@qq.com")
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
        log.info("超级管理员账号初始化成功");
    }
}

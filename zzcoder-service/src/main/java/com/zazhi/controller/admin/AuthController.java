package com.zazhi.controller.admin;

import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import com.zazhi.result.Result;
import com.zazhi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/11/9
 * @description: 管理员权限管理相关接口
 */
@RestController("adminAuthController")
@RequestMapping("/api/admin")
@Validated
@Slf4j
@Tag(name = "管理员权限管理相关接口")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/role")
    @Operation(summary = "添加角色") // TODO 权限管理
    public Result addRole(String roleName, String description) {
        log.info("添加角色：{}, {}", roleName, description);
        authService.addRole(roleName, description);
        return Result.success();
    }

    @PutMapping("/role")
    @Operation(summary = "修改角色") // TODO 权限管理
    public Result updateRole(@RequestBody Role role) {
        log.info("修改角色：{}", role);
        authService.updateRole(role);
        return Result.success();
    }

    @DeleteMapping("/role")
    @Operation(summary = "删除角色") // TODO 权限管理
    public Result deleteRole(Integer id) {
        log.info("删除角色：{}", id);
        authService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/role")
    @Operation(summary = "获取所有角色") // TODO 权限管理
    public Result<List<Role>> getRoles() {
        log.info("获取所有角色");
        return Result.success(authService.getRoles());
    }

    //获取所有权限
    @GetMapping("/permission")
    @Operation(summary = "获取所有权限") // TODO 权限管理
    public Result<List<Permission>> getPermissions() {
        log.info("获取所有权限");
        return Result.success(authService.getPermissions());
    }

    @PostMapping("/add-permission-to-role")
    @Operation(summary = "添加权限到角色") // TODO 权限管理
    public Result addPermissionToRole(Integer roleId, Integer permissionId) {
        log.info("添加权限到角色：{}, {}", roleId, permissionId);
        authService.addPermissionToRole(roleId, permissionId);
        return Result.success();
    }

    @PostMapping("/add-role-to-user")
    @Operation(summary = "添加角色到用户") // TODO 权限管理
    public Result addRoleToUser(Integer roleId, Integer userId) {
        log.info("添加角色到用户：{}, {}", roleId, userId);
        authService.addRoleToUser(roleId, userId);
        return Result.success();
    }


}

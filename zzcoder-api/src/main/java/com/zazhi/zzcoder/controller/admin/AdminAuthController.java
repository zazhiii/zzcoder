package com.zazhi.zzcoder.controller.admin;

import com.zazhi.zzcoder.common.pojo.entity.Permission;
import com.zazhi.zzcoder.common.pojo.entity.Role;
import com.zazhi.zzcoder.common.pojo.result.Result;
import com.zazhi.zzcoder.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/11/9
 * @description: 管理员权限管理相关接口
 */
@RestController
@RequestMapping("/api/admin/auth")
@Slf4j
@Tag(name = "权限管理")
@RequiredArgsConstructor
public class AdminAuthController {
    private final AuthService authService;

    @PostMapping("/role")
    @Operation(summary = "添加角色")
    public Result<Void> addRole(String roleName, String description) {
        log.info("添加角色：{}, {}", roleName, description);
        authService.addRole(roleName, description);
        return Result.success();
    }

    @PutMapping("/role")
    @Operation(summary = "修改角色描述")
    public Result<Void> updateRoleDesc(String description, Integer id) {
        log.info("修改角色：{}", id);
        authService.updateRoleDesc(description, id);
        return Result.success();
    }

    @DeleteMapping("/role")
    @Operation(summary = "删除角色")
    public Result<Void> deleteRole(Integer id) {
        log.info("删除角色：{}", id);
        authService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/role")
    @Operation(summary = "获取所有角色")
    public Result<List<Role>> getRoles() {
        log.info("获取所有角色");
        return Result.success(authService.getRoles());
    }

    //获取所有权限
    @GetMapping("/permission")
    @Operation(summary = "获取所有权限")
    public Result<List<Permission>> getPermissions() {
        log.info("获取所有权限");
        return Result.success(authService.getPermissions());
    }

    @PostMapping("/add-permission-to-role")
    @Operation(summary = "添加权限到角色")
    public Result<Void> addPermissionToRole(Integer roleId, Integer permissionId) {
        log.info("添加权限到角色：{}, {}", roleId, permissionId);
        authService.addPermissionToRole(roleId, permissionId);
        return Result.success();
    }

    @PostMapping("/add-role-to-user")
    @Operation(summary = "添加角色到用户")
    public Result<Void> addRoleToUser(Integer roleId, Integer userId) {
        log.info("添加角色到用户：{}, {}", roleId, userId);
        authService.addRoleToUser(roleId, userId);
        return Result.success();
    }

    @DeleteMapping("/remove-role-from-user")
    @Operation(summary = "从用户移除角色")
    public Result<Void> removeRoleFromUser(Integer roleId, Integer userId) {
        log.info("从用户移除角色：{}, {}", roleId, userId);
        authService.removeRoleFromUser(roleId, userId);
        return Result.success();
    }

    @DeleteMapping("/remove-permission-from-role")
    @Operation(summary = "从角色移除权限")
    public Result<Void> removePermissionFromRole(Integer roleId, Integer permissionId) {
        log.info("从角色移除权限：{}, {}", roleId, permissionId);
        authService.removePermissionFromRole(roleId, permissionId);
        return Result.success();
    }
}

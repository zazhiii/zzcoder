package com.zazhi.controller.admin;

import com.zazhi.pojo.entity.Permission;
import com.zazhi.pojo.entity.Role;
import com.zazhi.pojo.result.Result;
import com.zazhi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zazhi.common.constant.PermissionConstants.*;

/**
 * @author zazhi
 * @date 2024/11/9
 * @description: 管理员权限管理相关接口
 */
@RestController
@RequestMapping("/api/admin/auth")
// @Validated
// fix: @Validated「加在类上」会导致带有加了shiro鉴权认证注解的接口所在的类无法被knife4j扫描到。加在方法上不会有这个问题。
@Slf4j
@Tag(name = "权限管理")
public class AdminAuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/role")
    @Operation(summary = "添加角色")
    @RequiresAuthentication
    @RequiresPermissions(ROLE_ADD)
    public Result addRole(String roleName, String description) {
        log.info("添加角色：{}, {}", roleName, description);
        authService.addRole(roleName, description);
        return Result.success();
    }

    @PutMapping("/role")
    @Operation(summary = "修改角色")
    @RequiresAuthentication
    @RequiresPermissions(ROLE_UPDATE)
    public Result updateRole(@RequestBody Role role) {
        log.info("修改角色：{}", role);
        authService.updateRole(role);
        return Result.success();
    }

    @DeleteMapping("/role")
    @Operation(summary = "删除角色")
    @RequiresAuthentication
    @RequiresPermissions(ROLE_DELETE)
    public Result deleteRole(Integer id) {
        log.info("删除角色：{}", id);
        authService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/role")
    @Operation(summary = "获取所有角色")
    @RequiresAuthentication
    @RequiresPermissions(ROLE_LIST)
    public Result<List<Role>> getRoles() {
        log.info("获取所有角色");
        return Result.success(authService.getRoles());
    }

    //获取所有权限
    @GetMapping("/permission")
    @Operation(summary = "获取所有权限")
    @RequiresAuthentication
    @RequiresPermissions(PERMISSION_LIST)
    public Result<List<Permission>> getPermissions() {
        log.info("获取所有权限");
        return Result.success(authService.getPermissions());
    }

    @PostMapping("/add-permission-to-role")
    @Operation(summary = "添加权限到角色")
    @RequiresAuthentication
    @RequiresPermissions(ROLE_ADD_PERMISSION)
    public Result addPermissionToRole(Integer roleId, Integer permissionId) {
        log.info("添加权限到角色：{}, {}", roleId, permissionId);
        authService.addPermissionToRole(roleId, permissionId);
        return Result.success();
    }

    @PostMapping("/add-role-to-user")
    @Operation(summary = "添加角色到用户")
    @RequiresAuthentication
    @RequiresPermissions(USER_ADD_ROLE)
    public Result addRoleToUser(Integer roleId, Long userId) {
        log.info("添加角色到用户：{}, {}", roleId, userId);
        authService.addRoleToUser(roleId, userId);
        return Result.success();
    }


}

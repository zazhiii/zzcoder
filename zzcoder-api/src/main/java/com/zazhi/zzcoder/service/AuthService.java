package com.zazhi.zzcoder.service;

import com.zazhi.zzcoder.common.pojo.dto.*;
import com.zazhi.zzcoder.common.pojo.entity.Permission;
import com.zazhi.zzcoder.common.pojo.entity.Role;

import java.util.List;

public interface AuthService {

    /**
     * 更新用户的密码
     *
     * @param updatePasswordDTO 更新密码信息
     * @param token             用户token
     */
    void updatePassword(UpdatePasswordDTO updatePasswordDTO, String token);

    /**
     * 用户注册
     *
     * @param registerDTO 用户名、密码、邮箱、验证码
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return token
     */
    String login(LoginDTO loginDTO);

    /**
     * 通过邮箱验证码登录
     *
     * @param loginByEmailDTO 邮箱、验证码
     * @return token
     */
    String loginByEmail(LoginByEmailDTO loginByEmailDTO);

    /**
     * 通过邮箱验证码更新密码
     *
     * @param updatePasswordByEmailDTO 邮箱、验证码、新密码
     */
    void updatePasswordByEmail(UpdatePasswordByEmailDTO updatePasswordByEmailDTO);

    /**
     * 添加角色
     *
     * @param roleName 角色名
     */
    void addRole(String roleName, String description);

    /**
     * 更新角色信息
     *
     * @param description 角色描述
     * @param id          角色ID
     */
    void updateRoleDesc(String description, Integer id);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(Integer id);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<Role> getRoles();

    /**
     * 添加权限到角色
     *
     * @param roleId
     * @param permissionId
     */
    void addPermissionToRole(Integer roleId, Integer permissionId);

    /**
     * 添加角色到用户
     *
     * @param roleId
     * @param userId
     */
    void addRoleToUser(Integer roleId, Integer userId);

    /**
     * 获取所有权限
     *
     * @return
     */
    List<Permission> getPermissions();

    /**
     * 发送邮箱验证码
     *
     * @param sendCodeDTO 发送信息
     */
    void sendEmailCode(SendCodeDTO sendCodeDTO);

    /**
     * 登出
     *
     * @param token 用户token
     */
    void logout(String token);

    /**
     * 从用户移除角色
     *
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    void removeRoleFromUser(Integer roleId, Integer userId);

    /**
     * 从角色移除权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    void removePermissionFromRole(Integer roleId, Integer permissionId);
}

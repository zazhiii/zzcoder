package com.zazhi.service;

import com.zazhi.pojo.dto.*;
import com.zazhi.pojo.entity.Permission;
import com.zazhi.pojo.entity.User;
import com.zazhi.pojo.entity.Role;

import java.util.List;

public interface AuthService {

    /**
     * 更新用户的密码
     * @param updatePasswordDTO
     * @param token
     */
    void updatePsw(UpdatePasswordDTO updatePasswordDTO, String token);

    /**
     * 用户注册
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    String login(LoginDTO loginDTO);

    /**
     * 通过邮箱验证码登录
     * @param loginByEmailDTO
     * @return
     */
    String loginByEmail(LoginByEmailDTO loginByEmailDTO);

    /**
     * 通过邮箱验证码更新密码
     * @param updatePasswordByEmailDTO
     */
    void updatePswByEmail(UpdatePasswordByEmailDTO updatePasswordByEmailDTO);

    /**
     * 添加角色
     * @param roleName
     */
    void addRole(String roleName, String description);

    /**
     * 更新角色信息
     * @param role
     */
    void updateRole(Role role);

    /**
     * 删除角色
     * @param id
     */
    void deleteRole(Integer id);

    /**
     * 获取所有角色
     * @return
     */
    List<Role> getRoles();

    /**
     * 添加权限到角色
     * @param roleId
     * @param permissionId
     */
    void addPermissionToRole(Integer roleId, Integer permissionId);

    /**
     * 添加角色到用户
     * @param roleId
     * @param userId
     */
    void addRoleToUser(Integer roleId, Long userId);

    /**
     * 获取所有权限
     * @return
     */
    List<Permission> getPermissions();

    /**
     * 发送邮箱验证码
     * @param sendCodeDTO
     */
    void sendEmailCode(SendCodeDTO sendCodeDTO);

    /**
     * 登出
     * @param token
     */
    void logout(String token);
}

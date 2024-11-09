package com.zazhi.service;

import com.zazhi.dto.*;
import com.zazhi.entity.Permission;
import com.zazhi.entity.User;
import com.zazhi.entity.Role;

import java.util.List;

public interface AuthService {

    /**
     * 通过邮箱查找用户
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 通过手机号查询用户
     * @param phoneNumber
     * @return
     */
    User findByPhoneNumber(String phoneNumber);

    /**
     * 通过id查询用户
     *
     * @param userId
     * @return
     */
    User findUserById(Long userId);

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
    void addRoleToUser(Integer roleId, Integer userId);

    /**
     * 获取所有权限
     * @return
     */
    List<Permission> getPermissions();
}

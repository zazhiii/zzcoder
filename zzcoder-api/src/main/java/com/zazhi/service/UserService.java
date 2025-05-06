package com.zazhi.service;

import com.zazhi.pojo.dto.UpdateEmailDTO;
import com.zazhi.pojo.dto.UserInfoDTO;
import com.zazhi.pojo.entity.Permission;
import com.zazhi.pojo.entity.Role;
import com.zazhi.pojo.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    User getUserById(Long userId);

    /**
     * 获取用户基本信息
     * @return
     */
    UserInfoDTO getUserInfo();

    /**
     * 更新用户邮箱
     * @param updateEmailDTO
     */
    void updateEmail(UpdateEmailDTO updateEmailDTO);

    /**
     * 更新用户头像
     * @param avatarUrl
     */
    void updateAvatar(String avatarUrl);

    /**
     * 查询用户角色
     * @param userId
     * @return
     */
    List<Role> getUserRolesById(Long userId);

    /**
     * 查询用户权限
     * @param roles
     * @return
     */
    List<Permission> getUserPermissionsByRoles(List<Role> roles);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User getUserByName(String username);
}

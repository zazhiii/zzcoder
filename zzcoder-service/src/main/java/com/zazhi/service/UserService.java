package com.zazhi.service;

import com.zazhi.dto.UpdateEmailDTO;
import com.zazhi.dto.UserInfoDTO;
import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import com.zazhi.entity.User;

import java.util.List;
import java.util.Set;

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
}

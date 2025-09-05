package com.zazhi.service;

import com.zazhi.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.common.pojo.dto.UserInfoDTO;
import com.zazhi.common.pojo.dto.UserUpdateDTO;
import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.common.pojo.vo.RoleAndPermissionVO;
import com.zazhi.common.pojo.vo.UserSubmitStatVO;

import java.util.List;

public interface UserService {

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    User getUserById(Integer userId);

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
     * 更新用户信息
     * @param userInfo
     */
    void updateUserInfo(UserUpdateDTO userInfo);

    /**
     * 获取用户提交统计信息
     * @return 提交统计信息
     */
    UserSubmitStatVO getSubmitStat();

    /**
     * 获取用户角色和权限信息
     * @return 角色和权限信息
     */
    RoleAndPermissionVO getRoleAndPermission(Integer userId);
}

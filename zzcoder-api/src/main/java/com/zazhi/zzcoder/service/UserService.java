package com.zazhi.zzcoder.service;

import com.zazhi.zzcoder.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.zzcoder.common.pojo.dto.UserInfoVO;
import com.zazhi.zzcoder.common.pojo.dto.UserUpdateDTO;
import com.zazhi.zzcoder.common.pojo.entity.User;
import com.zazhi.zzcoder.common.pojo.vo.RoleAndPermissionVO;
import com.zazhi.zzcoder.common.pojo.vo.UserSubmitStatVO;
import com.zazhi.zzcoder.security.RoleAndPermission;

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
    UserInfoVO getUserInfo();

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
    RoleAndPermission getRoleAndPermission(Integer userId);
}

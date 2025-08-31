package com.zazhi.service;

import com.zazhi.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.common.pojo.dto.UserInfoDTO;
import com.zazhi.common.pojo.dto.UserUpdateDTO;
import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.common.pojo.entity.User;
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
     * 查询用户角色
     * @param userId
     * @return
     */
    List<Role> getUserRolesById(Integer userId);

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

    /**
     * 更新用户信息
     * @param userInfo
     */
    void updateUserInfo(UserUpdateDTO userInfo);

    /**
     * 获取用户已解决题目数
     * @return 题目数量
     */
    Integer getSolvedProblemCount();

    /**
     * 获取用户通过次数
     * @return 通过次数
     */
    Integer getSubmissionCount();

    /**
     * 获取用户通过次数
     * @return 通过次数
     */
    Integer getAcCount();

    /**
     * 获取用户提交统计信息
     * @return 提交统计信息
     */
    UserSubmitStatVO getSubmitStat();
}

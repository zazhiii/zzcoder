package com.zazhi.service.impl;

import com.zazhi.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.common.pojo.dto.UserInfoDTO;
import com.zazhi.common.pojo.dto.UserUpdateDTO;
import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.common.pojo.vo.UserSubmitStatVO;
import com.zazhi.exception.model.VerificationCodeException;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.UserService;
import com.zazhi.common.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户相关业务
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    VerificationCodeService verificationCodeService;

    /**
     * 根据用户id查询用户
     *
     * @param userId
     * @return
     */
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }

    /**
     * 获取用户基本信息
     *
     * @return
     */
    public UserInfoDTO getUserInfo() {
        Integer userId = ThreadLocalUtil.getCurrentId();
        User user = userMapper.findById(userId);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);

        // 获取用户角色和权限
        List<Role> roles = userMapper.getUserRolesById(userId);
        List<Permission> permissions = userMapper.findPermissionsByRoles(roles);
        userInfoDTO.setRoles(roles.stream().map(Role::getName).toList());
        userInfoDTO.setPermissions(permissions.stream().map(Permission::getName).toList());

        return userInfoDTO;
    }

    /**
     * 更新用户邮箱
     *
     * @param updateEmailDTO
     */
    public void updateEmail(UpdateEmailDTO updateEmailDTO) {
        String newEmail = updateEmailDTO.getNewEmail();
        String code = updateEmailDTO.getEmailVerificationCode();
        if (verificationCodeService.verifyCode(newEmail, code)) {
            User user = new User();
            Integer userId = ThreadLocalUtil.getCurrentId();
            user.setId(userId);
            user.setEmail(newEmail);
            userMapper.update(user);
        } else {
            throw new VerificationCodeException();
        }
    }

    /**
     * 更新用户头像
     *
     * @param avatarUrl
     */
    public void updateAvatar(String avatarUrl) {
        Integer userId = ThreadLocalUtil.getCurrentId();
        User user = new User();
        user.setAvatarUrl(avatarUrl);
        user.setId(userId);
        userMapper.update(user);
    }

    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    public List<Role> getUserRolesById(Integer userId) {
        return userMapper.getUserRolesById(userId);
    }

    /**
     * 查询用户权限
     *
     * @param roles
     * @return
     */
    public List<Permission> getUserPermissionsByRoles(List<Role> roles) {
        return userMapper.findPermissionsByRoles(roles);
    }

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {
        return userMapper.getByName(username);
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    @Override
    public void updateUserInfo(UserUpdateDTO userInfo) {
        User user = new User();
        BeanUtils.copyProperties(userInfo, user);
        user.setId(ThreadLocalUtil.getCurrentId());
        userMapper.update(user);
    }

    @Override
    public Integer getSolvedProblemCount() {
        Integer userId = ThreadLocalUtil.getCurrentId();
        return userMapper.getSolvedProblemCount(userId);
    }

    @Override
    public Integer getSubmissionCount() {
        Integer userId = ThreadLocalUtil.getCurrentId();
        return userMapper.getSubmissionCount(userId);
    }


    @Override
    public Integer getAcCount() {
        return 0;
    }

    /**
     * 获取用户提交统计信息
     *
     * @return
     */
    @Override
    public UserSubmitStatVO getSubmitStat() {
        return userMapper.getSubmitStat(ThreadLocalUtil.getCurrentId());
    }


}

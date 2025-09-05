package com.zazhi.service.impl;

import cn.hutool.json.JSONUtil;
import com.zazhi.common.constants.RedisKeyConstants;
import com.zazhi.common.enums.EmailCodeBizType;
import com.zazhi.common.exception.code.AuthError;
import com.zazhi.common.exception.model.BizException;
import com.zazhi.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.common.pojo.dto.UserInfoDTO;
import com.zazhi.common.pojo.dto.UserUpdateDTO;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.common.pojo.vo.RoleAndPermissionVO;
import com.zazhi.common.pojo.vo.UserSubmitStatVO;
import com.zazhi.common.utils.RedisUtil;
import com.zazhi.common.utils.ThreadLocalUtil;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.zazhi.common.constants.RedisKeyConstants.USER_ROLE_PERMISSIONS;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户相关业务
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    private final RedisUtil redisUtil;

    /**
     * 根据用户id查询用户
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }

    /**
     * 获取用户基本信息
     *
     * @return UserInfoDTO
     */
    public UserInfoDTO getUserInfo() {
        // 基本信息
        Integer userId = ThreadLocalUtil.getCurrentId();
        User user = userMapper.findById(userId);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);

        // 角色和权限
        RoleAndPermissionVO roleAndPermissionVO = userMapper.getRoleAndPermissionByUserId(userId);
        userInfoDTO.setRoles(roleAndPermissionVO.getRoles());
        userInfoDTO.setPermissions(roleAndPermissionVO.getPermissions());

        return userInfoDTO;
    }

    /**
     * 更新用户邮箱
     *
     * @param updateEmailDTO 更新邮箱DTO
     */
    public void updateEmail(UpdateEmailDTO updateEmailDTO) {
        String newEmail = updateEmailDTO.getNewEmail();
        String code = updateEmailDTO.getCode();

        // 校验验证码
        String key = RedisKeyConstants.format(RedisKeyConstants.EMAIL_CODE,
                EmailCodeBizType.UPDATE_EMAIL.getCode(), newEmail);
        String storeCode = redisUtil.get(key);
        if (storeCode == null || !storeCode.equals(code)) {
            throw new BizException(AuthError.EMAIL_CODE_INCORRECT_OR_EXPIRED);
        }

        User user = User.builder()
                .id(ThreadLocalUtil.getCurrentId())
                .email(newEmail)
                .build();
        userMapper.update(user);
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

    /**
     * 获取用户提交统计信息
     *
     * @return
     */
    @Override
    public UserSubmitStatVO getSubmitStat() {
        return userMapper.getSubmitStat(ThreadLocalUtil.getCurrentId());
    }

    @Override
    public RoleAndPermissionVO getRoleAndPermission(Integer userId) {
        // 先从缓存中获取
        String rolePermission = redisUtil.get(RedisKeyConstants.format(USER_ROLE_PERMISSIONS, userId));
        if (rolePermission != null) {
            return JSONUtil.toBean(rolePermission, RoleAndPermissionVO.class);
        }
        // 缓存中没有，从数据库中获取
        RoleAndPermissionVO roleAndPermissionVO = userMapper.getRoleAndPermissionByUserId(userId);
        // 放入缓存
        String jsonStr = JSONUtil.toJsonStr(roleAndPermissionVO);
        redisUtil.set(RedisKeyConstants.format(USER_ROLE_PERMISSIONS, userId), jsonStr, 7, TimeUnit.DAYS);

        return roleAndPermissionVO;
    }
}

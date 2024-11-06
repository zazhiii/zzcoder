package com.zazhi.service.impl;

import com.zazhi.dto.UpdateEmailDTO;
import com.zazhi.dto.UserInfoDTO;
import com.zazhi.entity.User;
import com.zazhi.exception.VerificationCodeException;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.UserService;
import com.zazhi.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 获取用户基本信息
     * @return
     */
    public UserInfoDTO getUserInfo() {
        Long userId = ThreadLocalUtil.getCurrentId();
        User user = userMapper.findById(userId);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        return userInfoDTO;
    }

    /**
     * 更新用户邮箱
     * @param updateEmailDTO
     */
    public void updateEmail(UpdateEmailDTO updateEmailDTO) {
        String newEmail = updateEmailDTO.getNewEmail();
        String code = updateEmailDTO.getEmailVerificationCode();
        if (verificationCodeService.verifyCode(newEmail, code)){
            User user = new User();
            Long userId = ThreadLocalUtil.getCurrentId();
            user.setId(userId);
            user.setEmail(newEmail);
            userMapper.update(user);
        }else{
            throw new VerificationCodeException();
        }
    }

    /**
     * 更新用户头像
     * @param avatarUrl
     */
    public void updateAvatar(String avatarUrl) {
        Long userId = ThreadLocalUtil.getCurrentId();
        User user = new User();
        user.setAvatarUrl(avatarUrl);
        user.setId(userId);
        userMapper.update(user);
    }
}

package com.zazhi.service.impl;

import com.zazhi.constant.RegexConstant;
import com.zazhi.utils.Md5Util;
import com.zazhi.dto.RegisterDTO;
import com.zazhi.entity.User;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 用户相关业务
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过邮箱查找用户
     * @param email
     * @return
     */
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * 通过手机号查询用户
     * @param phoneNumber
     * @return
     */
    public User findByPhoneNumber(String phoneNumber) {
        return userMapper.findByPhoneNumber(phoneNumber);
    }

    /**
     * 通过用户名、手机号、邮箱查找用户
     * @param identification
     * @return
     */
    public User findUserByIdentification(String identification) {
        if (identification.matches(RegexConstant.EMAIL_REGEX)) {
            return findByEmail(identification);
        } else if (identification.matches(RegexConstant.PHONE_REGEX)) {
            log.info("在以手机号登录，{}", identification);
            return findByPhoneNumber(identification);
        } else {
            log.info("在以用户名登录，{}", identification);
            return findByUsername(identification);
        }
    }

    /**
     * 新增用户
     * @param registerDTO
     */
    public void add(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(Md5Util.getMD5String(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        userMapper.insert(user);
    }

    /**
     * 通过id查询用户
     *
     * @param userId
     * @return
     */
    public User findUserById(Long userId) {
        return userMapper.findById(userId);
    }

    /**
     * 更新用户的密码
     * @param id
     * @param password
     */
    public void updatePsw(Long id, String password) {
        userMapper.updatePsw(id, Md5Util.getMD5String(password));
    }
}

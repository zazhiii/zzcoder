package com.zazhi.service;

import com.zazhi.dto.*;
import com.zazhi.entity.User;

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
}

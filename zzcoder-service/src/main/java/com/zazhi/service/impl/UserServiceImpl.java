package com.zazhi.service.impl;

import com.zazhi.common.utils.Md5Util;
import com.zazhi.dto.RegisterDTO;
import com.zazhi.entity.User;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 用户相关业务
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过邮箱查找用户
     *
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
}

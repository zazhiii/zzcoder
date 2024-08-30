package com.zazhi.service;

import com.zazhi.dto.RegisterDTO;
import com.zazhi.entity.User;

public interface UserService {

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
     * 新增用户
     * @param registerDTO
     */
    void add(RegisterDTO registerDTO);
}

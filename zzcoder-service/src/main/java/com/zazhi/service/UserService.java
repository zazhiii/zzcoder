package com.zazhi.service;

import com.zazhi.dto.UpdateEmailDTO;
import com.zazhi.dto.UserInfoDTO;
import com.zazhi.result.Result;

public interface UserService {

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
}

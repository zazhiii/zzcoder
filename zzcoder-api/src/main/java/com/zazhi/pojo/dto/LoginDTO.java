package com.zazhi.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    /**
     * 用户名或手机号或邮箱
     */
    @NotBlank(message = "用户名或手机号或邮箱不能为空")
    private String identification;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
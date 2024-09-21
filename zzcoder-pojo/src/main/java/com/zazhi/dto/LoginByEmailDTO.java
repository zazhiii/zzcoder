package com.zazhi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.io.Serializable;

@Data
public class LoginByEmailDTO implements Serializable {
    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 用户收到的邮箱验证码，6位数字或字母
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "验证码必须是6位数字或字母")
    private String emailVerificationCode;
}

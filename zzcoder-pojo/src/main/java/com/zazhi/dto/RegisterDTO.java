package com.zazhi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {
    /**
     * 用户名，需唯一
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 32, message = "用户名长度应在3到32个字符之间")
    private String username;
    /**
     * 用户密码，长度在6-64个字符之间，建议使用强密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度应在6到64个字符之间")
    private String password;
    /**
     * 用户邮箱，需唯一
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 用户收到的邮箱验证码，通常为6位数字或字母
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "验证码必须是6位数字或字母")
    private String emailVerificationCode;
}

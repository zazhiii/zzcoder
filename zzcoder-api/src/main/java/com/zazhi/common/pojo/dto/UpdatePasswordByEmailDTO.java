package com.zazhi.common.pojo.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.io.Serializable;

@Data
public class UpdatePasswordByEmailDTO implements Serializable {
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
    private String emailCode;
    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    @Size(min = 6, max = 64, message = "新密码长度需在6-64个字符之间")
    private String newPassword;

}

package com.zazhi.common.pojo.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

@Data
public class UpdatePasswordDTO implements Serializable {
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;

    @NotEmpty(message = "新密码不能为空")
    @Size(min = 6, max = 64, message = "新密码长度需在6-64个字符之间")
    private String newPassword;
}

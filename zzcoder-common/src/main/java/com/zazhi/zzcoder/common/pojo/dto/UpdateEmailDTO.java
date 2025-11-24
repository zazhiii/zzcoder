package com.zazhi.zzcoder.common.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2024/11/1
 * @description: 更新邮箱dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String newEmail;

    @NotBlank(message = "验证码不能为空")
    private String code;
}

package com.zazhi.common.pojo.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * @author zazhi
 * @date 2025/7/11
 * @description: 发送邮箱验证码 DTO
 */
@Data
public class SendCodeDTO {
    @Email
    private String email;        // 目标邮箱
    private String businessType; // 业务类型，如 register、login、reset_password 等
}

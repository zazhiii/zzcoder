package com.zazhi.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 接收发送邮箱验证码数据
 */
@Data
public class SendEmailCodeDTO implements Serializable {

    @Email
    private String email;

    private String purpose;
}

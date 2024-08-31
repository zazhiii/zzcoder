package com.zazhi.controller;

import com.zazhi.common.constant.ErrorMsg;
import com.zazhi.common.constant.ValidationMsg;
import com.zazhi.common.result.Result;
import com.zazhi.entity.User;
import com.zazhi.service.UserService;
import com.zazhi.dto.*;
import com.zazhi.service.impl.VerificationCodeService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 用户相关接口
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    @GetMapping("/send-email-verification-code")
    public Result sendEmailVerificationCode(
            @RequestParam
            @Email(message = ValidationMsg.INVALID_EMAIL_FORMAT)
            String email){

        // 发送验证码
        verificationCodeService.sendVerificationCode(email);
        return Result.success();
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterDTO registerDTO){
        //判断邮箱是否注册
        User user = userService.findByEmail(registerDTO.getEmail());
        if(user != null){
            return Result.error(ErrorMsg.EMAIL_ALREADY_REGISTERED);
        }

        // 判断用户名是否注册
        user = userService.findByUsername(registerDTO.getUsername());
        if(user != null){
            return Result.error(ErrorMsg.USERNAME_ALREADY_REGISTERED);
        }

        //判断验证码是否正确
        if(!verificationCodeService.verifyCode(registerDTO.getEmail(), registerDTO.getEmailVerificationCode())){
            return Result.error(ErrorMsg.INVALID_VERIFICATION_CODE);
        }

        userService.add(registerDTO);
        return Result.success();
    }
}

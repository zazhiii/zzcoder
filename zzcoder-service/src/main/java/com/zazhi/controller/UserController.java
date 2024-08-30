package com.zazhi.controller;

import com.zazhi.common.result.Result;
import com.zazhi.entity.User;
import com.zazhi.service.UserService;
import com.zazhi.dto.*;
import com.zazhi.service.impl.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 用户相关接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    @PostMapping("/send-email-verification-code")
    public Result sendEmailVerificationCode(@RequestBody @Validated SendEmailCodeDTO sendEmailCodeDTO){
        // 若是注册验证码，判断邮箱是否已注册
        if(sendEmailCodeDTO.getPurpose().equals("register")){
            User user = userService.findByEmail(sendEmailCodeDTO.getEmail());
            System.out.println(user);
            if(user != null) return Result.error("该邮箱已注册！");
        }

        // 发送验证码
        verificationCodeService.sendVerificationCode(sendEmailCodeDTO.getEmail());
        return Result.success();
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterDTO registerDTO){
        // 判断用户名是否注册
        User user = userService.findByUsername(registerDTO.getUsername());
        if(user != null){
            return Result.error("该用户名已注册！");
        }

        //判断验证码是否正确
        if(!verificationCodeService.verifyCode(registerDTO.getEmail(), registerDTO.getEmailVerificationCode())){
            return Result.error("验证码不正确或已过期！");
        }

        userService.add(registerDTO);
        return Result.success();
    }
}

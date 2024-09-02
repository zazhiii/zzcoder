package com.zazhi.controller;

import com.zazhi.common.constant.ErrorMsg;
import com.zazhi.common.constant.RegexConstant;
import com.zazhi.common.constant.ValidationMsg;
import com.zazhi.common.result.Result;
import com.zazhi.common.utils.JwtUtil;
import com.zazhi.common.utils.Md5Util;
import com.zazhi.common.utils.RedisUtil;
import com.zazhi.entity.User;
import com.zazhi.service.UserService;
import com.zazhi.dto.*;
import com.zazhi.service.impl.VerificationCodeService;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 用户相关接口
 */
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/send-email-verification-code")
    public Result sendEmailVerificationCode(
            @RequestParam
            @Email(message = ValidationMsg.INVALID_EMAIL_FORMAT)
            String email){

        // 发送验证码
        log.info("开始发送验证码，{}", email);
        verificationCodeService.sendVerificationCode(email);
        return Result.success();
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterDTO registerDTO){
        log.info("开始注册：{}", registerDTO);
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

    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginDTO loginDTO){
        String identification = loginDTO.getIdentification();
        String password = loginDTO.getPassword();

        User user = userService.findUserByIdentification(identification);
        // 查找到该用户且密码正确
        if(user != null && user.getPassword().equals(Md5Util.getMD5String(password))){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            String token = JwtUtil.genToken(claims);

            // 登录后 token 存入redis
            redisUtil.set(token, token, 7, TimeUnit.DAYS);

            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }
}

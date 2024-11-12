package com.zazhi.controller;

import com.zazhi.result.Result;
import com.zazhi.service.AuthService;
import com.zazhi.utils.RedisUtil;
import com.zazhi.dto.*;
import com.zazhi.service.impl.VerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 注册、登录、更改密码相关接口
 */
@RestController
@RequestMapping("/api")
@Validated
@Slf4j
@Tag(name = "注册、登录、更改密码、权限相关接口")
public class AuthController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisUtil redisUtil;


    @GetMapping("/send-email-verification-code")
    @Operation(summary = "发送邮箱验证码")
    public Result sendEmailVerificationCode(@RequestParam @Email String email){
        log.info("开始发送验证码，{}", email);

        verificationCodeService.sendVerificationCode(email);
        return Result.success();
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册") // TODO 给新用户分配角色
    public Result register(@RequestBody @Validated RegisterDTO registerDTO){
        log.info("开始注册：{}", registerDTO);

        authService.register(registerDTO);
        return Result.success();
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@Validated @RequestBody LoginDTO loginDTO){
        log.info("用户正在登录：{}", loginDTO);

        String token = authService.login(loginDTO);
        return Result.success(token);
    }

    @PostMapping("/login-by-email-code")
    @Operation(summary = "通过邮箱验证码登录")
    public Result<String> loginByEmail(@Validated @RequestBody LoginByEmailDTO loginByEmailDTO){
        log.info("用户通过邮箱登录:，{}", loginByEmailDTO.getEmail());

        String token = authService.loginByEmail(loginByEmailDTO);
        return Result.success(token);
    }

    @PostMapping("/update-password")
    @Operation(summary = "更新密码")
    public Result updatePsw(@Validated @RequestBody UpdatePasswordDTO updatePasswordDTO, @RequestHeader("Authorization") String token){
        log.info("更新密码");

        // 更新密码
        authService.updatePsw(updatePasswordDTO, token);
        return Result.success();
    }

    @PostMapping("/update-password-by-email")
    @Operation(summary = "通过邮箱验证码更改密码")
    public Result updatePswByEmail(@Validated @RequestBody UpdatePasswordByEmailDTO updatePasswordByEmailDTO){
        log.info("通过邮箱更新密码：{}", updatePasswordByEmailDTO.getEmail());

        authService.updatePswByEmail(updatePasswordByEmailDTO);
        return Result.success();
    }

    @GetMapping("/logout")
    @Operation(summary = "登出")
    public Result logout(@RequestHeader("Authorization") String token){
        log.info("登出：{}", token);

        redisUtil.delete(token);
        return Result.success();
    }
}

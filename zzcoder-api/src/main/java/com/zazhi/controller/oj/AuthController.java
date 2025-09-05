package com.zazhi.controller.oj;

import com.zazhi.common.pojo.dto.*;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 注册、登录、更改密码相关接口
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Auth 相关接口")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/send-email-code")
    @Operation(summary = "发送邮箱验证码")
    public Result<Void> sendEmailCode(@Validated SendCodeDTO sendCodeDTO){
        log.info("开始发送验证码，{}", sendCodeDTO.getEmail());
        authService.sendEmailCode(sendCodeDTO);
        return Result.success();
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Void> register(@RequestBody @Validated RegisterDTO registerDTO){
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
    public Result<Void> updatePsw(@Validated @RequestBody UpdatePasswordDTO updatePasswordDTO, @RequestHeader("Authorization") String token){
        log.info("更新密码");
        authService.updatePassword(updatePasswordDTO, token);
        return Result.success();
    }

    @PostMapping("/update-password-by-email")
    @Operation(summary = "通过邮箱验证码更改密码")
    public Result<Void> updatePswByEmail(@Validated @RequestBody UpdatePasswordByEmailDTO updatePasswordByEmailDTO){
        log.info("通过邮箱更新密码：{}", updatePasswordByEmailDTO.getEmail());

        authService.updatePasswordByEmail(updatePasswordByEmailDTO);
        return Result.success();
    }

    @GetMapping("/logout")
    @Operation(summary = "登出")
    @RequiresAuthentication
    public Result<Void> logout(@RequestHeader("Authorization") String token){
        log.info("登出：{}", token);
        authService.logout(token);
        return Result.success();
    }
}

package com.zazhi.controller;

import com.zazhi.common.constant.ErrorMsg;
import com.zazhi.common.constant.ValidationMsg;
import com.zazhi.common.result.Result;
import com.zazhi.common.utils.JwtUtil;
import com.zazhi.common.utils.Md5Util;
import com.zazhi.common.utils.RedisUtil;
import com.zazhi.common.utils.ThreadLocalUtil;
import com.zazhi.entity.User;
import com.zazhi.service.UserService;
import com.zazhi.dto.*;
import com.zazhi.service.impl.VerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户")
public class UserController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;


    @GetMapping("/send-email-verification-code")
    @Operation(summary = "发送邮箱验证码")
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
    @Operation(summary = "用户注册")
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
    @Operation(summary = "用户登录")
    public Result<String> login(@Validated @RequestBody LoginDTO loginDTO){
        String identification = loginDTO.getIdentification();
        String password = loginDTO.getPassword();

        User user = userService.findUserByIdentification(identification);
        // 查找到该用户且密码正确
        if(user != null && user.getPassword().equals(Md5Util.getMD5String(password))){
            // 生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            String token = JwtUtil.genToken(claims);

            // 登录后 token 存入redis {token: token}的形式
            redisUtil.set(token, token, 7, TimeUnit.DAYS);

            return Result.success(token);
        }else{
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/login-by-email-code")
    @Operation(summary = "通过邮箱验证码登录")
    public Result<String> loginByEmail(@Validated @RequestBody LoginByEmailDTO loginByEmailDTO){
        log.info("用户通过邮箱登录:，{}", loginByEmailDTO.getEmail());

        String email = loginByEmailDTO.getEmail();
        String code = loginByEmailDTO.getEmailVerificationCode();
        User user = userService.findByEmail(email);

        if(user == null){
            return Result.error("用户不存在");
        }
        if(!verificationCodeService.verifyCode(email, code)){
            return Result.error("验证码不正确或已过期");
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);

        // token 存入redis
        redisUtil.set(token, token, 7, TimeUnit.DAYS);

        return Result.success(token);
    }

    @PostMapping("/update-password")
    @Operation(summary = "更新密码")
    public Result updatePsw(@Validated @RequestBody UpdatePasswordDTO updatePasswordDTO, @RequestHeader("Authorization") String token){
        log.info("更新密码");

        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = (Long) map.get("id");

        User user = userService.findUserById(userId);
        if(!Md5Util.getMD5String(updatePasswordDTO.getOldPassword()).equals(user.getPassword())){
            return Result.error("原密码不正确");
        }

        // 更新密码
        userService.updatePsw(userId, updatePasswordDTO.getNewPassword());

        // 删除旧token
        redisUtil.delete(token);

        return Result.success();
    }

    @PostMapping("/update-password-by-email")
    @Operation(summary = "通过邮箱验证码更改密码")
    public Result updatePswByEmail(@Validated @RequestBody UpdatePasswordByEmailDTO updatePasswordByEmailDTO){
        log.info("通过邮箱更新密码：{}", updatePasswordByEmailDTO.getEmail());

        String email = updatePasswordByEmailDTO.getEmail();
        String code = updatePasswordByEmailDTO.getEmailVerificationCode();
        //判断用户是否存在
        User user = userService.findByEmail(updatePasswordByEmailDTO.getEmail());

        if(user == null){
            return Result.error("用户不存在");
        }
        if(!verificationCodeService.verifyCode(email, code)){
            return Result.error("验证码错误或已过期");
        }

        //更新密码
        userService.updatePsw(user.getId(), updatePasswordByEmailDTO.getNewPassword());

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

package com.zazhi.controller.oj;

import com.zazhi.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.common.pojo.dto.UserInfoDTO;
import com.zazhi.common.pojo.dto.UserUpdateDTO;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户相关接口
 */
@RestController
@RequestMapping("/api/user")
//@Validated
@Slf4j
@Tag(name = "用户相关接口")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    @Operation(summary = "获取用户基本信息")
    @RequiresAuthentication
    public Result<UserInfoDTO> getUerInfo(){
        log.info("获取用户基本信息");
        return Result.success(userService.getUserInfo());
    }

    @PutMapping("/email")
    @Operation(summary = "更新用户邮箱")
    public Result updateEmail(@RequestBody UpdateEmailDTO updateEmailDTO){
        log.info("更新邮箱");

        userService.updateEmail(updateEmailDTO);
        return Result.success();
    }

    @PutMapping("/avatar")
    @Operation(summary = "上传用户头像")
    public Result updateAvatar(@RequestParam String avatarUrl){
        log.info("更新用户头像，{}", avatarUrl);
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PutMapping()
    @Operation(summary = "更新用户基本信息")
    public Result<Void> updateUserInfo(@RequestBody UserUpdateDTO userInfo) {
        log.info("更新用户信息：{}", userInfo);
        userService.updateUserInfo(userInfo);
        return Result.success();
    }
}

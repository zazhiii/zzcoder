package com.zazhi.zzcoder.controller.oj;

import com.zazhi.zzcoder.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.zzcoder.common.pojo.dto.UserInfoVO;
import com.zazhi.zzcoder.common.pojo.dto.UserUpdateDTO;
import com.zazhi.zzcoder.common.pojo.result.Result;
import com.zazhi.zzcoder.common.pojo.vo.UserSubmitStatVO;
import com.zazhi.zzcoder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户相关接口
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "用户")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    @Operation(summary = "获取用户基本信息")
    public Result<UserInfoVO> getUerInfo(){
        log.info("获取用户基本信息");
        return Result.success(userService.getUserInfo());
    }

    @PutMapping("/email")
    @Operation(summary = "更新用户邮箱")
    public Result<Void> updateEmail(@RequestBody UpdateEmailDTO updateEmailDTO){
        log.info("更新邮箱");
        userService.updateEmail(updateEmailDTO);
        return Result.success();
    }

    @PutMapping("/avatar")
    @Operation(summary = "上传用户头像")
    public Result<Void> updateAvatar(@RequestParam String avatarUrl){
        log.info("上传用户头像，{}", avatarUrl);
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户基本信息")
    public Result<Void> updateUserInfo(@RequestBody UserUpdateDTO userInfo) {
        log.info("更新用户信息：{}", userInfo);
        userService.updateUserInfo(userInfo);
        return Result.success();
    }

    @GetMapping("/submit-stat")
    @Operation(summary = "获取用户提交统计信息")
    public Result<UserSubmitStatVO> getSubmitStat(){
        log.info("获取用户提交统计信息");
        return Result.success(userService.getSubmitStat());
    }
}

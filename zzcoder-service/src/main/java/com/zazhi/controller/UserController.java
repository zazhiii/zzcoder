package com.zazhi.controller;

import com.zazhi.dto.UpdateEmailDTO;
import com.zazhi.dto.UserInfoDTO;
import com.zazhi.result.Result;
import com.zazhi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户相关接口
 */
@RestController
@RequestMapping("/api/user")
@Validated
@Slf4j
@Tag(name = "用户相关接口")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    @Operation(summary = "获取用户基本信息")
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

}

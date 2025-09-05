package com.zazhi.controller.oj;

import com.zazhi.common.pojo.dto.UpdateEmailDTO;
import com.zazhi.common.pojo.dto.UserInfoDTO;
import com.zazhi.common.pojo.dto.UserUpdateDTO;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.common.pojo.vo.UserSubmitStatVO;
import com.zazhi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

    @GetMapping("/submit-stat")
    @Operation(summary = "获取用户提交统计信息")
    @RequiresAuthentication
    public Result<UserSubmitStatVO> getSubmitStat(){
        log.info("获取用户提交统计信息");
        return Result.success(userService.getSubmitStat());
    }

//    @GetMapping("/solved-count")
//    @Operation(summary = "已解决题目数量")
//    @RequiresAuthentication
//    public Result<Integer> getSolvedProblemCount(){
//        log.info("获取用户已解决题目数量");
//        Integer count = userService.getSolvedProblemCount();
//        return Result.success(count);
//    }
//
//    @GetMapping("/submission-count")
//    @Operation(summary = "提交记录数量")
//    @RequiresAuthentication
//    public Result<Integer> getSubmissionCount(){
//        log.info("获取用户提交记录数量");
//        Integer count = userService.getSubmissionCount();
//        return Result.success(count);
//    }
//
//    @GetMapping("/ac-count")
//    @Operation(summary = "通过次数")
//    @RequiresAuthentication
//    public Result<Integer> getAcCount(){
//        log.info("获取用户通过次数");
//        Integer count = userService.getAcCount();
//        return Result.success(count);
//    }

}

package com.zazhi.judger.controller;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.common.pojo.Result;
import com.zazhi.judger.service.JudgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zazhi
 * @date 2025/7/10
 * @description: JudgeController 类用于处理判题请求的控制器
 */
@RestController
@RequestMapping("api/judge")
@RequiredArgsConstructor
@Tag(name = "测评代码", description = "判题控制器")
public class JudgeController {

    private final JudgeService judgeService;

    @PostMapping
    @Operation(summary = "执行判题任务", description = "提交代码进行判题")
    public Result<JudgeResult> judge(@RequestBody JudgeTask judgeTask){
        return Result.success(judgeService.judge(judgeTask));
    }

}
